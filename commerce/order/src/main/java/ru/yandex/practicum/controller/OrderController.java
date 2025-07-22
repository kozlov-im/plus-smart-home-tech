package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.request.CreateNewOrderRequest;
import ru.yandex.practicum.request.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Collection<OrderDto> getUserOrders(String username) {
        log.info("getUserOrders {}", username);
        return orderService.getUserOrders(username);
    }


    @PutMapping
    public OrderDto createNewOrder(@Valid @RequestBody CreateNewOrderRequest request) {
        log.info("createNewOrder request {}", request);
        return orderService.createNewOrder(request);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(@Valid @RequestBody ProductReturnRequest request) {
        log.info("returnOrder request {}", request);
        return orderService.returnOrder(request);
    }

    @PostMapping("/payment")
    public OrderDto createOrderPayment(@RequestBody UUID orderId) {
        log.info("createOrderPayment request for order{}", orderId);
        return orderService.createOrderPayment(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto setOrderPaymentFailed(@RequestBody UUID orderId) {
        log.info("setOrderPaymentFailed request for order{}", orderId);
        return orderService.setOrderPaymentFailed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto setOrderDeliveryDelivered(@RequestBody UUID orderId) {
        log.info("setOrderDeliveryDelivered request for order{}", orderId);
        return orderService.setOrderDeliveryDelivered(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto setOrderDeliveryFailed(@RequestBody UUID orderId) {
        log.info("setOrderDeliveryFailed request for order{}", orderId);
        return orderService.setOrderDeliveryFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto setOrderCompleted(@RequestBody UUID orderId) {
        log.info("setOrderCompleted request for order{}", orderId);
        return orderService.setOrderCompleted(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateOrderTotalPrice(@RequestBody UUID orderId) {
        log.info("calculateOrderTotalPrice request for order{}", orderId);
        return orderService.calculateOrderTotalPrice(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateOrderDelivery(@RequestBody UUID orderId) {
        log.info("calculateOrderDelivery request for order{}", orderId);
        return orderService.calculateOrderDelivery(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assembleOrder(@RequestBody UUID orderId) {
        log.info("assembleOrder request for order{}", orderId);
        return orderService.assembleOrder(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto setOrderAssembleFailed(@RequestBody UUID orderId) {
        log.info("setOrderAssembleFailed request for order{}", orderId);
        return orderService.setOrderAssembleFailed(orderId);
    }

    @PostMapping("/payment/success")
    public OrderDto setOrderPaymentSuccess(@RequestBody UUID orderId) {
        log.info("setOrderPaymentSuccess request for order{}", orderId);
        return orderService.setOrderPaymentSuccess(orderId);
    }

    @GetMapping("/id")
    public OrderDto getOrderById(@RequestParam UUID orderId) {
        log.info("getOrderById {}", orderId);
        return orderService.getOrderById(orderId);
    }

    @PostMapping("/delivery/success")
    public OrderDto setOrderDeliverySuccess(@RequestBody UUID orderId) {
        log.info("setOrderDeliverySuccess request for order{}", orderId);
        return orderService.setOrderDeliverySuccess(orderId);
    }



    @GetMapping("/test")
    public String test() {
        return "Order is running";
    }


}
