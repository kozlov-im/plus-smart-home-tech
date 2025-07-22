package ru.yandex.practicum.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.request.CreateNewOrderRequest;
import ru.yandex.practicum.request.ProductReturnRequest;

import java.util.Collection;
import java.util.UUID;

public interface OrderService {

    Collection<OrderDto> getUserOrders(String username);

    OrderDto createNewOrder(CreateNewOrderRequest request);

    OrderDto returnOrder(ProductReturnRequest request);

    OrderDto createOrderPayment(UUID orderId);

    OrderDto setOrderPaymentFailed(UUID orderId);

    OrderDto setOrderDeliveryDelivered(UUID orderId);

    OrderDto setOrderDeliveryFailed(UUID orderId);

    OrderDto setOrderCompleted(UUID orderId);

    OrderDto calculateOrderTotalPrice(UUID orderId);

    OrderDto calculateOrderDelivery(UUID orderId);

    OrderDto assembleOrder(UUID orderId);

    OrderDto setOrderAssembleFailed(UUID orderId);

    OrderDto setOrderPaymentSuccess(UUID orderId);

    OrderDto getOrderById(UUID orderId);

    OrderDto setOrderDeliverySuccess(UUID orderId);
}
