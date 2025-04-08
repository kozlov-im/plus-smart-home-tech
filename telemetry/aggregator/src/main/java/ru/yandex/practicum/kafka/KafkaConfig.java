package ru.yandex.practicum.kafka;

import lombok.Data;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;
import ru.yandex.practicum.kafka.deserializer.SensorEventDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.Properties;

@Configuration
@ConfigurationProperties("aggregator.kafka")
@Data
public class KafkaConfig {

    private String bootstrapServer;
    private String customerClientId;
    private String customerGroupId;

    @Bean
    KafkaClient getClient() {
        return new KafkaClient() {

            private Producer<String, SpecificRecordBase> producer;
            private Consumer<String, SensorEventAvro> consumer;

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                if (producer == null) {
                    initProducer();
                }
                return producer;
            }

            private void initProducer() {
                Properties properties = new Properties();
                properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
                properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
                producer = new KafkaProducer<>(properties);

            }

            @Override
            public Consumer<String, SensorEventAvro> getConsumer() {
                if (consumer == null) {
                    initConsumer();
                }
                return consumer;
            }

            private void initConsumer() {
                Properties properties = new Properties();
                properties.put(ConsumerConfig.CLIENT_ID_CONFIG, customerClientId);
                properties.put(ConsumerConfig.GROUP_ID_CONFIG, customerGroupId);
                properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
                properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
                properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
                consumer = new KafkaConsumer<>(properties);
            }
        };
    }
}
