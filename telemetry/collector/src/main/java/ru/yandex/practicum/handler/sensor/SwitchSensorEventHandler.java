package ru.yandex.practicum.handler.sensor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import java.time.Instant;

@Component
@Data
@ConfigurationProperties("topics")
@Slf4j
public class SwitchSensorEventHandler implements SensorEventHandler {
    private final KafkaClient kafkaClient;
    private String telemetrySensors;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
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
        log.info("Into {} sent SwitchSensor {}", telemetrySensors, eventAvro);
    }

    private SensorEventAvro mapToAvro(SensorEventProto eventProto) {
        SwitchSensorProto switchSensorProto = eventProto.getSwitchSensorEvent();
        SwitchSensorAvro switchSensorAvro = SwitchSensorAvro.newBuilder()
                .setState(switchSensorProto.getState())
                .build();
        return SensorEventAvro.newBuilder()
                .setId(eventProto.getId())
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                        eventProto.getTimestamp().getNanos()))
                .setPayload(switchSensorAvro)
                .build();
    }
}
