package ru.yandex.practicum.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.handler.SnapshotHandler;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SnapshotProcessor {
    private final KafkaClient kafkaClient;
    private final SnapshotHandler snapshotHandler;

    @Value(value = "${snapshotTopic}")
    private String topic;

    public void start() {
        Consumer<String, SensorsSnapshotAvro> consumer = kafkaClient.getSnapshotConsumer();
        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(topic));
            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    SensorsSnapshotAvro snapshotAvro = record.value();
                    snapshotHandler.handle(snapshotAvro);
                    log.info("Analyzer got snapshot {}", snapshotAvro);
                }
            }
        } catch (Exception e) {
            log.error("Snapshot consumer got an error: ", e);
        }
    }
}
