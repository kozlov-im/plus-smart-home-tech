package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createNewDelivery(DeliveryDto deliveryDto);

    DeliveryDto setDeliverySuccessful(UUID deliveryId);

    DeliveryDto pickDelivery(UUID deliveryId);

    DeliveryDto setDeliveryFailed(UUID deliveryId);

    BigDecimal calculateDeliveryCost(OrderDto orderDto);

    DeliveryDto setDeliveryCancel(UUID deliveryId);
}
