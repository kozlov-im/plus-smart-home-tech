package ru.yandex.practicum.processor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.handler.HubEventHandler;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
@Data
@ConfigurationProperties("topics")
public class HubEventProcessor implements Runnable {
    private final KafkaClient kafkaClient;
    private final HubEventHandler hubEventHandler;

    private String telemetryHubs;
    private int consumerAttemptTimeout;

    @Override
    public void run() {
        Consumer<String, HubEventAvro> consumer = kafkaClient.getHubConsumer();
        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(telemetryHubs));

            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(Duration.ofMillis(consumerAttemptTimeout));

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventAvro hubEventAvro = record.value();
                    hubEventHandler.handle(hubEventAvro);
                    log.info("Analyzer got hubEvent from {} {}", telemetryHubs, hubEventAvro);
                }
                consumer.commitSync();
            }
        } catch (Exception e) {
            log.error("Hub consumer got an error: ", e);
        }
    }

}
