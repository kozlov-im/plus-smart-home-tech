package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.feignClient.DeliveryClient;
import ru.yandex.practicum.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController implements DeliveryClient {

    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto createNewDelivery(@Valid @RequestBody DeliveryDto deliveryDto) {
        log.info("createNewDelivery request {}", deliveryDto);
        return deliveryService.createNewDelivery(deliveryDto);
    }

    @Override
    public DeliveryDto setDeliverySuccessful(@RequestBody UUID deliveryId) {
        log.info("setDeliverySuccessful request {}", deliveryId);
        return deliveryService.setDeliverySuccessful(deliveryId);
    }

    @Override
    public DeliveryDto pickDelivery(@RequestBody UUID deliveryId) {
        log.info("pickDelivery request {}", deliveryId);
        return deliveryService.pickDelivery(deliveryId);
    }

    @Override
    public DeliveryDto setDeliveryFailed(@RequestBody UUID deliveryId) {
        log.info("setDeliveryFailed request {}", deliveryId);
        return deliveryService.setDeliveryFailed(deliveryId);
    }

    @Override
    public BigDecimal calculateDeliveryCost(@Valid @RequestBody OrderDto orderDto) {
        log.info("calculateDeliveryCost request {}", orderDto);
        return deliveryService.calculateDeliveryCost(orderDto);
    }

    @Override
    public DeliveryDto setDeliveryCancel(@RequestBody UUID deliveryId) {
        log.info("setDeliveryCancel request {}", deliveryId);
        return deliveryService.setDeliveryCancel(deliveryId);
    }

    @GetMapping("/test")
    public String test() {
        return "Delivery is running";
    }
}
