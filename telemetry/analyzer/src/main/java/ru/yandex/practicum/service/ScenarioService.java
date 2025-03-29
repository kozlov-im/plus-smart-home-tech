package ru.yandex.practicum.service;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.model.enums.ActionType;
import ru.yandex.practicum.model.enums.ConditionOperation;
import ru.yandex.practicum.model.enums.ConditionType;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;

    public void addScenario(ScenarioAddedEventAvro event, String hubId, HubEventAvro hubEvent) {

        Optional<Scenario> scenarioOptional = scenarioRepository.findByHubIdAndName(hubId, event.getName());

        if (scenarioOptional.isPresent()) {
            scenarioRepository.delete(scenarioOptional.get());
            createScenario(event, hubId, hubEvent);
            log.info("Scenario={} was updated", scenarioOptional.get().getName());
        } else {
            createScenario(event, hubId, hubEvent);
        }
    }

    private void createScenario(ScenarioAddedEventAvro event, String hubId, HubEventAvro hubEvent) {
        Scenario scenario = new Scenario();
        scenario.setName(event.getName());
        scenario.setHubId(hubId);

        List<Condition> conditions = event.getConditions().stream()
                .map(conditionEvent -> Condition.builder()
                        .sensor(Sensor.builder().id(conditionEvent.getSensorId()).hubId(hubId).build())
                        .type(ConditionType.valueOf(conditionEvent.getType().name()))
                        .operation(ConditionOperation.valueOf(conditionEvent.getOperation().name()))
                        .value(convertToInteger(conditionEvent.getValue()))
                        .scenario(scenario)
                        .build()).toList();

        List<Action> actions = event.getActions().stream()
                .map(actionEvent -> Action.builder()
                        .sensor(Sensor.builder().id(actionEvent.getSensorId()).hubId(hubId).build())
                        .type(ActionType.valueOf(actionEvent.getType().name()))
                        .value(actionEvent.getValue() != null ? actionEvent.getValue() : 0)
                        .scenario(scenario)
                        .build()).toList();
        scenario.setConditions(conditions);
        scenario.setActions(actions);
        scenarioRepository.save(scenario);
        log.info("Scenario={} added; {}, Event {}", scenario.getName(), scenario, hubEvent);
    }

    public void removeScenario (ScenarioRemovedEventAvro event, String hubId) {
        Optional<Scenario> scenarioOptional = scenarioRepository.findByHubIdAndName(hubId, event.getName());
        scenarioOptional.ifPresent(scenarioRepository::delete);
    }

    private Integer convertToInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        } else {
            return null;
        }
    }

}
