package ru.yandex.practicum.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class MotionSensorEvent implements SensorEventHandler {
    private final KafkaClient kafkaClient;

    @Value(value = "${sensorEventTopic}")
    private String topic;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_PROTO;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        SensorEventAvro eventAvro = mapToAvro(eventProto);

        kafkaClient.getProducer().send(new ProducerRecord<>(
                topic,
                null,
                eventAvro.getTimestamp().toEpochMilli(),
                eventAvro.getHubId(),
                eventAvro));
        log.trace("into topic {} was send event {}", topic, eventAvro);
    }

    private SensorEventAvro mapToAvro(SensorEventProto eventProto) {
        MotionSensorProto motionSensorProto = eventProto.getMotionSensorProto();
        MotionSensorAvro motionSensorAvro = MotionSensorAvro.newBuilder()
                .setLinkQuality(motionSensorProto.getLinkQuality())
                .setMotion(motionSensorProto.getMotion())
                .setVoltage(motionSensorProto.getVoltage())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(eventProto.getId())
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                        eventProto.getTimestamp().getNanos()))
                .setPayload(motionSensorAvro)
                .build();
    }
}
