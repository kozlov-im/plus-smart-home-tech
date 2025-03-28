package ru.yandex.practicum.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.service.ScenarioService;
import ru.yandex.practicum.service.SensorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventHandler {
    private final SensorService sensorService;
    private final ScenarioService scenarioService;

    public void handle(HubEventAvro event) {
        switch (event.getPayload()) {
            case DeviceAddedEventAvro deviceAddedEventAvro ->
                    sensorService.addSensor(deviceAddedEventAvro.getId(), event.getHubId());
            case DeviceRemovedEventAvro deviceRemovedEventAvro ->
                    sensorService.removeSensor(deviceRemovedEventAvro.getId(), event.getHubId());
            case ScenarioAddedEventAvro scenarioAddedEventAvro ->
                    scenarioService.addScenario(scenarioAddedEventAvro, event.getHubId());
            case ScenarioRemovedEventAvro scenarioRemovedEventAvro ->
                    scenarioService.removeScenario(scenarioRemovedEventAvro, event.getHubId());
            case null, default -> System.out.println("unknown event");
        }
    }
}
