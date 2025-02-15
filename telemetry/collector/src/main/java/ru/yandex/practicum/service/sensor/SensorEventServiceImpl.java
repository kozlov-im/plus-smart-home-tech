package ru.yandex.practicum.service.sensor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.sensor.*;

@Service
@AllArgsConstructor
@Slf4j
public class SensorEventServiceImpl implements SensorEventService {
    private final KafkaClient kafkaClient;

    @Override
    public void collect(SensorEvent event) {
        String topic = "telemetry.sensors.v1";
        SensorEventAvro sensorEventAvro = mapToAvro(event);
        kafkaClient.getProducer().send(new ProducerRecord<>(topic, sensorEventAvro));

    }

    private SensorEventAvro mapToAvro(SensorEvent event) {
        Object payload;
        if (event.getType().equals(SensorEventType.CLIMATE_SENSOR_EVENT)) {
            ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) event;
            payload = ClimateSensorAvro.newBuilder()
                    .setTemperatureC(climateSensorEvent.getTemperatureC())
                    .setHumidity(climateSensorEvent.getHumidity())
                    .setCo2Level(climateSensorEvent.getCo2Level())
                    .build();
        } else if (event.getType().equals(SensorEventType.LIGHT_SENSOR_EVENT)) {
            LightSensorEvent lightSensorEvent = (LightSensorEvent) event;
            payload = LightSensorAvro.newBuilder()
                    .setLinkQuality(lightSensorEvent.getLinkQuality())
                    .setLuminosity(lightSensorEvent.getLuminosity())
                    .build();
        } else if (event.getType().equals(SensorEventType.MOTION_SENSOR_EVENT)) {
            MotionSensorEvent motionSensorEvent = (MotionSensorEvent) event;
            payload = MotionSensorAvro.newBuilder()
                    .setLinkQuality(motionSensorEvent.getLinkQuality())
                    .setMotion(motionSensorEvent.isMotion())
                    .setVoltage(motionSensorEvent.getVoltage())
                    .build();
        } else if (event.getType().equals(SensorEventType.SWITCH_SENSOR_EVENT)) {
            SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) event;
            payload = SwitchSensorAvro.newBuilder()
                    .setState(switchSensorEvent.isState())
                    .build();
        } else if (event.getType().equals(SensorEventType.TEMPERATURE_SENSOR_EVENT)) {
            TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) event;
            payload = TemperatureSensorAvro.newBuilder()
                    .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                    .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                    .build();
        } else
            payload = null;
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
