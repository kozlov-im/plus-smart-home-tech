package ru.yandex.practicum.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceAddedEventHandler implements HubEventHandler {
    private final KafkaClient kafkaClient;

    @Value(value = "${hubEventTopic}")
    private String topic;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
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
        log.trace("into topic {} was send event {}", topic, eventAvro);
    }

    private HubEventAvro mapToAvro(HubEventProto eventProto) {
        DeviceAddedEventProto deviceAddedEventProto = eventProto.getDeviceAdded();
        DeviceAddedEventAvro deviceAddedEventAvro = DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEventProto.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEventProto.getType().name()))
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                        eventProto.getTimestamp().getNanos()))
                .setPayload(deviceAddedEventAvro)
                .build();
    }
}
