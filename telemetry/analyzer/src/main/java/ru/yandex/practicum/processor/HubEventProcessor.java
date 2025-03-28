package ru.yandex.practicum.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.handler.HubEventHandler;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HubEventProcessor implements Runnable {
    private final KafkaClient kafkaClient;
    private final HubEventHandler hubEventHandler;

    @Value(value = "${hubEventTopic}")
    private String topic;


    @Override
    public void run() {
        Consumer<String, HubEventAvro> consumer = kafkaClient.getHubConsumer();
        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(topic));

            while (!Thread.currentThread().isInterrupted()) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventAvro hubEventAvro = record.value();
                    hubEventHandler.handle(hubEventAvro);
                    log.info("Analyzer got hubEvent {}", hubEventAvro);
                }
            }
        } catch (Exception e) {
            log.error("Hub consumer got an error: ", e);
        }
    }

}
