package ru.yandex.practicum.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {

    @PutMapping
    DeliveryDto createNewDelivery(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/successful")
    DeliveryDto setDeliverySuccessful(@RequestBody UUID deliveryId);

    @PostMapping("/picked")
    DeliveryDto pickDelivery(@RequestBody UUID deliveryId);

    @PostMapping("/failed")
    DeliveryDto setDeliveryFailed(@RequestBody UUID deliveryId);

    @PostMapping("/cost")
    BigDecimal calculateDeliveryCost(@RequestBody OrderDto orderDto);

    @PostMapping("/cancelled")
    DeliveryDto setDeliveryCancel(@RequestBody UUID deliveryId);
}
