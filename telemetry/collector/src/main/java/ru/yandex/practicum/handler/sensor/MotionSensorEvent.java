package ru.yandex.practicum.handler.sensor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
@Data
@ConfigurationProperties("topics")
@Slf4j
public class MotionSensorEvent implements SensorEventHandler {
    private final KafkaClient kafkaClient;
    private String telemetrySensors;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        SensorEventAvro eventAvro = mapToAvro(eventProto);

        kafkaClient.getProducer().send(new ProducerRecord<>(
                telemetrySensors,
                null,
                eventAvro.getTimestamp().toEpochMilli(),
                eventAvro.getHubId(),
                eventAvro));
        log.info("Into {} sent MotionSensor {}", telemetrySensors, eventAvro);
    }

    private SensorEventAvro mapToAvro(SensorEventProto eventProto) {
        MotionSensorProto motionSensorProto = eventProto.getMotionSensorEvent();
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
