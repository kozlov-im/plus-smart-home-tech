package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.feignClient.OrderClient;
import ru.yandex.practicum.request.CreateNewOrderRequest;
import ru.yandex.practicum.request.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderClient {

    private final OrderService orderService;

    @Override
    public Collection<OrderDto> getUserOrders(String username) {
        log.info("getUserOrders {}", username);
        return orderService.getUserOrders(username);
    }


    @Override
    public OrderDto createNewOrder(@Valid @RequestBody CreateNewOrderRequest request) {
        log.info("createNewOrder request {}", request);
        return orderService.createNewOrder(request);
    }

    @Override
    public OrderDto returnOrder(@Valid @RequestBody ProductReturnRequest request) {
        log.info("returnOrder request {}", request);
        return orderService.returnOrder(request);
    }

    @Override
    public OrderDto createOrderPayment(@RequestBody UUID orderId) {
        log.info("createOrderPayment request for order{}", orderId);
        return orderService.createOrderPayment(orderId);
    }

    @Override
    public OrderDto setOrderPaymentFailed(@RequestBody UUID orderId) {
        log.info("setOrderPaymentFailed request for order{}", orderId);
        return orderService.setOrderPaymentFailed(orderId);
    }

    @Override
    public OrderDto setOrderDeliveryDelivered(@RequestBody UUID orderId) {
        log.info("setOrderDeliveryDelivered request for order{}", orderId);
        return orderService.setOrderDeliveryDelivered(orderId);
    }

    @Override
    public OrderDto setOrderDeliveryFailed(@RequestBody UUID orderId) {
        log.info("setOrderDeliveryFailed request for order{}", orderId);
        return orderService.setOrderDeliveryFailed(orderId);
    }

    @Override
    public OrderDto setOrderCompleted(@RequestBody UUID orderId) {
        log.info("setOrderCompleted request for order{}", orderId);
        return orderService.setOrderCompleted(orderId);
    }

    @Override
    public OrderDto calculateOrderTotalPrice(@RequestBody UUID orderId) {
        log.info("calculateOrderTotalPrice request for order{}", orderId);
        return orderService.calculateOrderTotalPrice(orderId);
    }

    @Override
    public OrderDto calculateOrderDelivery(@RequestBody UUID orderId) {
        log.info("calculateOrderDelivery request for order{}", orderId);
        return orderService.calculateOrderDelivery(orderId);
    }

    @Override
    public OrderDto assembleOrder(@RequestBody UUID orderId) {
        log.info("assembleOrder request for order{}", orderId);
        return orderService.assembleOrder(orderId);
    }

    @Override
    public OrderDto setOrderAssembleFailed(@RequestBody UUID orderId) {
        log.info("setOrderAssembleFailed request for order{}", orderId);
        return orderService.setOrderAssembleFailed(orderId);
    }

    @Override
    public OrderDto setOrderPaymentSuccess(@RequestBody UUID orderId) {
        log.info("setOrderPaymentSuccess request for order{}", orderId);
        return orderService.setOrderPaymentSuccess(orderId);
    }

    @Override
    public OrderDto getOrderById(@RequestParam UUID orderId) {
        log.info("getOrderById {}", orderId);
        return orderService.getOrderById(orderId);
    }

    @Override
    public OrderDto setOrderDeliverySuccess(@RequestBody UUID orderId) {
        log.info("setOrderDeliverySuccess request for order{}", orderId);
        return orderService.setOrderDeliverySuccess(orderId);
    }


    @GetMapping("/test")
    public String test() {
        return "Order is running";
    }

}
