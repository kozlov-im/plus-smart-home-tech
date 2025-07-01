package ru.yandex.practicum.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.consumer.Consumer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface KafkaClient {

    Producer<String, SpecificRecordBase> getProducer();

    Consumer<String, SensorEventAvro> getConsumer();

}
