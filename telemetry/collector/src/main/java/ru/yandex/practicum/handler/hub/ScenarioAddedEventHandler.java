package ru.yandex.practicum.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScenarioAddedEventHandler implements HubEventHandler {
    private final KafkaClient kafkaClient;

    @Value(value = "${hubEventTopic}")
    private String topic;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        HubEventAvro eventAvro = mapToAvro(eventProto);

        kafkaClient.getProducer().send(new ProducerRecord<>(
                topic,
                null,
                eventAvro.getTimestamp().toEpochMilli(),
                eventAvro.getHubId(),
                eventAvro));
        log.info("into topic {} was send event {}", topic, eventAvro);
    }

    private HubEventAvro mapToAvro(HubEventProto eventProto) {
        ScenarioAddedEventProto scenarioAddedEventProto = eventProto.getScenarioAdded();
        List<ScenarioConditionAvro> scenarioConditionAvroList = scenarioAddedEventProto.getConditionsList()
                .stream().map(this::mapToAvroScenarioCondition).toList();
        List<DeviceActionAvro> deviceActionAvroList = scenarioAddedEventProto.getActionsList()
                .stream().map(this::mapToAvroDeviceAction).toList();
        ScenarioAddedEventAvro scenarioAddedEventAvro = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEventProto.getName())
                .setConditions(scenarioConditionAvroList)
                .setActions(deviceActionAvroList)
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                        eventProto.getTimestamp().getNanos()))
                .setPayload(scenarioAddedEventAvro)
                .build();
    }

    private ScenarioConditionAvro mapToAvroScenarioCondition(ScenarioConditionProto scenarioConditionProto) {
      Object value = null;
      if (scenarioConditionProto.getValueCase().equals(ScenarioConditionProto.ValueCase.INT_VALUE)) {
          value = scenarioConditionProto.getIntValue();
      } else if (scenarioConditionProto.getValueCase().equals(ScenarioConditionProto.ValueCase.BOOL_VALUE)) {
          value = scenarioConditionProto.getBoolValue();
      }
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioConditionProto.getSensorId())
                .setType(ConditionTypeAvro.valueOf(scenarioConditionProto.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(scenarioConditionProto.getOperation().name()))
                .setValue(value)
                .build();
    }

    private DeviceActionAvro mapToAvroDeviceAction(DeviceActionProto deviceActionProto) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceActionProto.getSensorId())
                .setType(ActionTypeAvro.valueOf(deviceActionProto.getType().name()))
                .setValue(deviceActionProto.getValue())
                .build();
    }
}
