package ru.yandex.practicum.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.hub.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubEvenServiceImpl implements HubEventService {
    private final KafkaClient kafkaClient;

    @Value(value = "${hubEventTopic}")
    private String topic;

    @Override
    public void collect(HubEvent event) {
        HubEventAvro hubEventAvro = mapToAvro(event);
        if (hubEventAvro == null) throw new AssertionError();
        kafkaClient.getProducer().send(new ProducerRecord<>(
                topic,
                null,
                hubEventAvro.getTimestamp().toEpochMilli(),
                hubEventAvro.getHubId(),
                hubEventAvro));
        log.trace("into topic {} was send event {}", topic, event );

    }

    private HubEventAvro mapToAvro(HubEvent event) {
        Object payload;
        if (event.getType().equals(HubEventType.DEVICE_ADDED_EVENT)) {
            DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;
            payload = DeviceAddedEventAvro.newBuilder()
                    .setId(deviceAddedEvent.getId())
                    .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().name()))
                    .build();
        } else if (event.getType().equals(HubEventType.DEVICE_REMOVED_EVENT)) {
            DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) event;
            payload = DeviceRemovedEventAvro.newBuilder()
                    .setId(deviceRemovedEvent.getId())
                    .build();
        } else if (event.getType().equals(HubEventType.SCENARIO_ADDED_EVENT)) {
            ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;
            List<ScenarioConditionAvro> scenarioConditionAvroList = scenarioAddedEvent.getConditions().stream()
                    .map(this::mapToAvroScenarioCondition).toList();
            List<DeviceActionAvro> deviceActionAvroList = scenarioAddedEvent.getActions().stream()
                    .map(this::mapToAvroDeviceAction).toList();
            payload = ScenarioAddedEventAvro.newBuilder()
                    .setName(scenarioAddedEvent.getName())
                    .setConditions(scenarioConditionAvroList)
                    .setActions(deviceActionAvroList)
                    .build();
        } else if (event.getType().equals(HubEventType.DEVICE_REMOVED_EVENT)) {
            ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;
            payload = ScenarioRemovedEventAvro.newBuilder()
                    .setName(scenarioRemovedEvent.getName())
                    .build();
        } else {
            return null;
        }
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private ScenarioConditionAvro mapToAvroScenarioCondition(ScenarioCondition scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private DeviceActionAvro mapToAvroDeviceAction(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                .setValue(deviceAction.getValue())
                .build();
    }
}
