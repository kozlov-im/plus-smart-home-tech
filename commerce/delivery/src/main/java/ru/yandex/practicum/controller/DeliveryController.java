package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto createNewDelivery(@RequestBody DeliveryDto deliveryDto) {
        log.info("createNewDelivery request {}", deliveryDto);
        return deliveryService.createNewDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    public DeliveryDto setDeliverySuccessful(@RequestBody UUID deliveryId) {
        log.info("setDeliverySuccessful request {}", deliveryId);
        return deliveryService.setDeliverySuccessful(deliveryId);
    }

    @PostMapping("/picked")
    public DeliveryDto pickDelivery(@RequestBody UUID deliveryId) {
        log.info("pickDelivery request {}", deliveryId);
        return deliveryService.pickDelivery(deliveryId);
    }

    @PostMapping("/failed")
    public DeliveryDto setDeliveryFailed(@RequestBody UUID deliveryId) {
        log.info("setDeliveryFailed request {}", deliveryId);
        return deliveryService.setDeliveryFailed(deliveryId);
    }

    @PostMapping("/cost")
    public BigDecimal calculateDeliveryCost(@RequestBody OrderDto orderDto) {
        log.info("calculateDeliveryCost request {}", orderDto);
        return deliveryService.calculateDeliveryCost(orderDto);
    }

    @PostMapping("/cancelled")
    public DeliveryDto setDeliveryCancel(@RequestBody UUID deliveryId) {
        log.info("setDeliveryCancel request {}", deliveryId);
        return deliveryService.setDeliveryCancel(deliveryId);
    }

    @GetMapping("/test")
    public String test() {
        return "Delivery is running";
    }
}
