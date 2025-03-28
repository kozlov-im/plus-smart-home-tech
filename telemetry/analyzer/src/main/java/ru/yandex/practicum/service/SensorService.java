package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorService {
    private final SensorRepository sensorRepository;

    public void addSensor(String sensorId, String hubId) {
        if (sensorRepository.existsByIdInAndHubId(List.of(sensorId), hubId)) {
            return;
        }
        Sensor sensor = Sensor.builder()
                .id(sensorId)
                .hubId(hubId)
                .build();
        sensorRepository.save(sensor);
        log.info("device was added {}", sensor);
    }

    public void removeSensor(String sensorId, String hubId) {
        sensorRepository.findByIdAndHubId(sensorId, hubId).ifPresent(sensorRepository::delete);
        log.info("device with id {} was removed", sensorId);
    }

}
