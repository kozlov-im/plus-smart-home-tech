package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HandleRecord {
    private final Map<String, SensorsSnapshotAvro> snapshots;

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro eventAvro) {
        SensorsSnapshotAvro snapshotAvro;
        if (snapshots.containsKey(eventAvro.getId())) {
            snapshotAvro = snapshots.get(eventAvro.getId());
        } else {
            snapshotAvro = SensorsSnapshotAvro.newBuilder()
                    .setHubId(eventAvro.getHubId())
                    .setTimestamp(Instant.now())
                    .setSensorState(new HashMap<>())
                    .build();
        }
        Map<String, SensorStateAvro> sensorsStateAvro = snapshotAvro.getSensorState();
        if (isDataChanged(sensorsStateAvro, eventAvro)) {
            return Optional.empty();
        }

        SensorStateAvro sensorStateAvro = SensorStateAvro.newBuilder()
                .setData(eventAvro.getPayload())
                .setTimestamp(eventAvro.getTimestamp())
                .build();
        sensorsStateAvro.put(eventAvro.getId(), sensorStateAvro);
        snapshotAvro.setTimestamp(eventAvro.getTimestamp());
        snapshotAvro.setSensorState(sensorsStateAvro);

        return Optional.of(snapshotAvro);
    }

    private boolean isDataChanged(Map<String, SensorStateAvro> sensorsStateAvro, SensorEventAvro eventAvro) {
        if (sensorsStateAvro != null && sensorsStateAvro.containsKey(eventAvro.getId())) {
            SensorStateAvro oldState = sensorsStateAvro.get(eventAvro.getId());
            return oldState.getTimestamp().isAfter(eventAvro.getTimestamp()) ||
                    oldState.getData() == eventAvro.getPayload();
        }
        return false;
    }
}
