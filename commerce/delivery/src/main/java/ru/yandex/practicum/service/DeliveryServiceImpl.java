package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.feignClient.OrderClient;
import ru.yandex.practicum.feignClient.WarehouseClient;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.request.ShippedToDeliveryRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryMapper deliveryMapper;
    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(5.0);
    private static final BigDecimal WAREHOUSE_1_RATIO = BigDecimal.valueOf(1.0);
    private static final BigDecimal WAREHOUSE_2_RATIO = BigDecimal.valueOf(2.0);
    private static final BigDecimal FRAGILE_RATIO = BigDecimal.valueOf(0.2);
    private static final BigDecimal WEIGHT_RATIO = BigDecimal.valueOf(0.3);
    private static final BigDecimal VOLUME_RATIO = BigDecimal.valueOf(0.2);
    private static final BigDecimal STREET_RATIO = BigDecimal.valueOf(0.2);


    @Override
    public DeliveryDto createNewDelivery(DeliveryDto deliveryDto) {
        orderClient.getOrderById(deliveryDto.getOrderId());
        Delivery delivery = deliveryMapper.mapToDelivery(deliveryDto);
        return deliveryMapper.mapToDeliveryDto(deliveryRepository.save(delivery));
    }

    @Override
    public DeliveryDto setDeliverySuccessful(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.setOrderDeliverySuccess(delivery.getOrderId());
        return deliveryMapper.mapToDeliveryDto(deliveryRepository.save(delivery));
    }

    @Override
    public DeliveryDto pickDelivery(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        orderClient.assembleOrder(delivery.getOrderId());
        warehouseClient.shippedToDelivery(new ShippedToDeliveryRequest(delivery.getOrderId(), deliveryId));
        return deliveryMapper.mapToDeliveryDto(deliveryRepository.save(delivery));
    }

    @Override
    public DeliveryDto setDeliveryFailed(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.setOrderDeliveryFailed(delivery.getOrderId());
        return deliveryMapper.mapToDeliveryDto(deliveryRepository.save(delivery));
    }

    @Override
    public BigDecimal calculateDeliveryCost(OrderDto orderDto) {
        orderClient.getOrderById(orderDto.getOrderId());
        Delivery delivery = getDelivery(orderDto.getDeliveryId());
        Address warehouseAddress = delivery.getFromAddress();
        Address destinationAddress = delivery.getToAddress();

        BigDecimal tolalCost = BASE_RATE;

        tolalCost = tolalCost.add(warehouseAddress.getCity().equals("ADDRESS_1") ?
                tolalCost.multiply(WAREHOUSE_1_RATIO) : tolalCost.multiply(WAREHOUSE_2_RATIO));
        tolalCost = tolalCost.add(orderDto.isFragile() ? tolalCost.multiply(FRAGILE_RATIO) : BigDecimal.ZERO);
        tolalCost = tolalCost.add(BigDecimal.valueOf(orderDto.getDeliveryWeight()).multiply(WEIGHT_RATIO));
        tolalCost = tolalCost.add(BigDecimal.valueOf(orderDto.getDeliveryVolume()).multiply(VOLUME_RATIO));
        tolalCost = tolalCost.add(warehouseAddress.getStreet().equals(destinationAddress.getStreet()) ?
                BigDecimal.ZERO : tolalCost.multiply(STREET_RATIO));

        return tolalCost;
    }

    @Override
    public DeliveryDto setDeliveryCancel(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.CANCELLED);
        return deliveryMapper.mapToDeliveryDto(deliveryRepository.save(delivery));
    }

    private Delivery getDelivery(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new NoDeliveryFoundException("Delivery " + deliveryId + " is not found")
        );
    }
}
