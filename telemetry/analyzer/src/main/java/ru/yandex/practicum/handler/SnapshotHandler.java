package ru.yandex.practicum.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpcClient.HubRouterClient;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.enums.ConditionOperation;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotHandler {
    private final ScenarioRepository scenarioRepository;
    private final HubRouterClient hubRouterClient;

    public void handle(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);

        scenarios.stream()
                .filter(scenario -> isScenarioFit(scenario, snapshot))
                .forEach(scenario -> executeActions(scenario.getActions(), hubId));
    }

    private boolean isScenarioFit(Scenario scenario, SensorsSnapshotAvro snapshot) {
        return scenario.getConditions().stream().allMatch(condition -> checkCondition(condition, snapshot));
    }

    private boolean checkCondition(Condition condition, SensorsSnapshotAvro snapshot) {
        SensorStateAvro sensorState = snapshot.getSensorState().get(condition.getSensor().getId());
        if (sensorState == null) {
            return false;
        }
        switch (condition.getType()) {
            case MOTION:
                if (sensorState.getData() instanceof MotionSensorAvro motionSensorAvro) {
                    int value = motionSensorAvro.getMotion() ? 1 : 0;
                    return executeCondition(value,
                            condition.getOperation(),
                            condition.getValue());
                }
                break;
            case LUMINOSITY:
                if (sensorState.getData() instanceof LightSensorAvro lightSensorAvro) {
                    return executeCondition(lightSensorAvro.getLuminosity(),
                            condition.getOperation(),
                            condition.getValue());
                }
                break;
            case SWITCH:
                if (sensorState.getData() instanceof SwitchSensorAvro switchSensorAvro) {
                    int value = switchSensorAvro.getState() ? 1 : 0;
                    return executeCondition(value, condition.getOperation(), condition.getValue());
                }
                break;
            case TEMPERATURE:
                System.out.println(sensorState.getData().getClass());
                if (sensorState.getData() instanceof ClimateSensorAvro climateSensorAvro) {
                    return executeCondition(climateSensorAvro.getTemperatureC(),
                            condition.getOperation(),
                            condition.getValue());
                }
                if (sensorState.getData() instanceof TemperatureSensorAvro temperatureSensorAvro) {
                    return executeCondition(temperatureSensorAvro.getTemperatureC(),
                            condition.getOperation(),
                            condition.getValue());
                }
                break;
            case CO2LEVEL:
                if (sensorState.getData() instanceof ClimateSensorAvro climateSensorAvro) {
                    return executeCondition(climateSensorAvro.getCo2Level(),
                            condition.getOperation(),
                            condition.getValue());
                }
                break;
            case HUMIDITY:
                if (sensorState.getData() instanceof ClimateSensorAvro climateSensorAvro) {
                    return executeCondition(climateSensorAvro.getHumidity(),
                            condition.getOperation(),
                            condition.getValue());
                }
                break;
            default:
                log.info("unknown datatype");
                return false;
        }
        return false;
    }

    private boolean executeCondition(int value, ConditionOperation operation, int triggerValue) {
        return switch (operation) {
            case EQUALS -> value == triggerValue;
            case GREATER_THAN -> value > triggerValue;
            case LOWER_THAN -> value < triggerValue;
        };
    }

    private void executeActions(List<Action> actions, String hubId) {
        for (Action action : actions) {
            hubRouterClient.executeAction(action, hubId);
        }
    }

}
