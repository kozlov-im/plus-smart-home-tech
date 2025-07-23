package ru.yandex.practicum.feignClient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.request.CreateNewOrderRequest;
import ru.yandex.practicum.request.ProductReturnRequest;

import java.util.Collection;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {

    @GetMapping
    Collection<OrderDto> getUserOrders(String username);

    @PutMapping
    OrderDto createNewOrder(@Valid @RequestBody CreateNewOrderRequest request);

    @PostMapping("/return")
    OrderDto returnOrder(@Valid @RequestBody ProductReturnRequest request);

    @PostMapping("/payment")
    OrderDto createOrderPayment(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto setOrderPaymentFailed(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto setOrderDeliveryDelivered(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto setOrderDeliveryFailed(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto setOrderCompleted(@RequestBody UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateOrderTotalPrice(@RequestBody UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateOrderDelivery(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assembleOrder(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto setOrderAssembleFailed(@RequestBody UUID orderId);

    @PostMapping("/payment/success")
    OrderDto setOrderPaymentSuccess(@RequestBody UUID orderId);

    @GetMapping("/id")
    OrderDto getOrderById(@RequestParam UUID orderId);

    @PostMapping("/delivery/success")
    OrderDto setOrderDeliverySuccess(@RequestBody UUID orderId);

}