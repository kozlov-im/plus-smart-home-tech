package ru.yandex.practicum;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.*;

@Component
@Slf4j
@ConfigurationProperties("topics")
@Data
public class AggregationStarter {
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private final RecordHandler recordHandler;
    private final KafkaClient kafkaClient;

    private String telemetrySensors;
    private String telemetrySnapshots;
    private int consumerAttemptTimeout;

    public void start() {
        Consumer<String, SensorEventAvro> consumer = kafkaClient.getConsumer();
        Producer<String, SpecificRecordBase> producer = kafkaClient.getProducer();

        try {
            consumer.subscribe(List.of(telemetrySensors));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(consumerAttemptTimeout));
                int count = 0;

                if (records.isEmpty()) {
                    continue;
                }

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    Optional<SensorsSnapshotAvro> sensorsSnapshotAvroOptional = recordHandler.updateState(record.value());
                    if (sensorsSnapshotAvroOptional.isPresent()) {
                        SensorsSnapshotAvro snapshotAvro = sensorsSnapshotAvroOptional.get();
                        ProducerRecord<String, SpecificRecordBase> producerRecord =
                                new ProducerRecord<>(telemetrySnapshots,
                                        null,
                                        snapshotAvro.getTimestamp().toEpochMilli(),
                                        snapshotAvro.getHubId(),
                                        snapshotAvro);
                        producer.send(producerRecord);
                        log.info("Into {} send snapshot {}", telemetrySnapshots, snapshotAvro);
                    }
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }

        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.info("sensor events get error", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("consumer was closed");
                consumer.close();
                log.info("producer was closed");
                producer.close();
            }
        }
    }

    private void manageOffsets(ConsumerRecord<String, SensorEventAvro> record, int count,
                               Consumer<String, SensorEventAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }

    }
}
