package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorService {
    private final SensorRepository sensorRepository;

    public void addSensor(DeviceAddedEventAvro device, HubEventAvro event) {
        String sensorId = device.getId();
        String hubId = event.getHubId();
        if (sensorRepository.existsByIdInAndHubId(List.of(sensorId), hubId)) {
            log.info("Device {} with sensorId={} already added", device.getType(),  sensorId);
            return;
        }
        Sensor sensor = Sensor.builder()
                .id(sensorId)
                .hubId(hubId)
                .build();
        sensorRepository.save(sensor);
        log.info("Device {} with sensorId={} added; Event ={}", device.getType(), sensor.getId(), event);
    }

    public void removeSensor(String sensorId, String hubId) {
        sensorRepository.findByIdAndHubId(sensorId, hubId).ifPresent(sensorRepository::delete);
        log.info("Device with sensorId={} removed", sensorId);
    }

}
