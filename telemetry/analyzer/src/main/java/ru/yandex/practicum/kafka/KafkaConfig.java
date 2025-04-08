package ru.yandex.practicum.kafka;

import lombok.Data;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.deserializer.HubEventDeserializer;
import ru.yandex.practicum.kafka.deserializer.SnapshotDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
@Data
@ConfigurationProperties("analyzer.kafka")
public class KafkaConfig {

    private String bootstrapServer;
    private String hubConsumerClientId;
    private String hubConsumerGroupId;
    private String snapshotConsumerClientId;
    private String snapshotConsumerGroupId;

    @Bean
    KafkaClient getClient() {
        return new KafkaClient() {

            private Consumer<String, HubEventAvro> hubConsumer;
            private Consumer<String, SensorsSnapshotAvro> snapshotConsumer;


            @Override
            public Consumer<String, HubEventAvro> getHubConsumer() {
                if (hubConsumer == null) {
                    initHubConsumer();
                }
                return hubConsumer;
            }

            @Override
            public Consumer<String, SensorsSnapshotAvro> getSnapshotConsumer() {
                if (snapshotConsumer == null) {
                    initSnapshotConsumer();
                }
                return snapshotConsumer;
            }

            private void initHubConsumer() {
                Properties properties = new Properties();
                properties.put(ConsumerConfig.CLIENT_ID_CONFIG, hubConsumerClientId);
                properties.put(ConsumerConfig.GROUP_ID_CONFIG, hubConsumerGroupId);
                properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
                properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class);
                hubConsumer = new KafkaConsumer<>(properties);
            }

            private void initSnapshotConsumer() {
                Properties properties = new Properties();
                properties.put(ConsumerConfig.CLIENT_ID_CONFIG, snapshotConsumerClientId);
                properties.put(ConsumerConfig.GROUP_ID_CONFIG, snapshotConsumerGroupId);
                properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
                properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SnapshotDeserializer.class);
                snapshotConsumer = new KafkaConsumer<>(properties);
            }
        };
    }
}
