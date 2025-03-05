package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RecordHandler {
    private final Map<String, SensorsSnapshotAvro> snapshots;

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = snapshots.getOrDefault(
                event.getHubId(),
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(event.getHubId())
                        .setTimestamp(Instant.now())
                        .setSensorState(new HashMap<>())
                        .build());

        SensorStateAvro oldState = snapshot.getSensorState().get(event.getId());
        if (oldState != null && (oldState.getTimestamp().isAfter(event.getTimestamp())
                || oldState.getData().equals(event.getPayload()))) {
            return Optional.empty();
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        snapshot.getSensorState().put(event.getId(), newState);
        snapshot.setTimestamp(event.getTimestamp());
        snapshots.put(event.getHubId(), snapshot);
        return Optional.of(snapshot);
    }
}
