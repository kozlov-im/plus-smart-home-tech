package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.feignClient.ShoppingCartClient;
import ru.yandex.practicum.feignClient.WarehouseClient;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.request.CreateNewOrderRequest;
import ru.yandex.practicum.request.ProductReturnRequest;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final WarehouseClient warehouseClient;
    private final ShoppingCartClient shoppingCartClient;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Override
    public Collection<OrderDto> getUserOrders(String username) {
        checkUserAuthorization(username);
        return orderMapper.mapToListOrderDto(orderRepository.findByUsername(username));
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest request) {
        String username = shoppingCartClient.getUsernameByShoppingCartId(request.getShoppingCart().getShoppingCartId());

        BookedProductsDto bookedProducts = warehouseClient.checkProductsForBooking(request.getShoppingCart(), "order");
        Order order = orderMapper.mapToOrder(request, bookedProducts);
        order.setUsername(username);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        Order order = getOrder(UUID.fromString(request.getOrderId()));
        warehouseClient.returnProduct(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        orderRepository.save(order);
        return orderMapper.mapToOrderDto(order);
    }

    @Override
    public OrderDto createOrderPayment(UUID orderId) {
        Order order = getOrder(orderId);

        //Реализовать оплату заказа

        order.setState(OrderState.ON_PAYMENT);
        return orderMapper.mapToOrderDto(order);
    }

    @Override
    public OrderDto setOrderPaymentFailed(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setOrderDeliveryDelivered(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setOrderDeliveryFailed(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setOrderCompleted(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.COMPLETED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateOrderTotalPrice(UUID orderId) {
        Order order = getOrder(orderId);

        //реализовать получение цены

        BigDecimal totalPrice = BigDecimal.valueOf(125.0);
        order.setTotalPrice(totalPrice);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateOrderDelivery(UUID orderId) {
        Order order = getOrder(orderId);

        //реализовать получение доставки

        BigDecimal deliveryPrice = BigDecimal.valueOf(25.0);
        order.setDeliveryPrice(deliveryPrice);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assembleOrder(UUID orderId) {
        Order order = getOrder(orderId);

        //реализовать сборку заказа;

        order.setState(OrderState.ASSEMBLED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setOrderAssembleFailed(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        return orderMapper.mapToOrderDto(orderRepository.save(order));
    }


    private void checkUserAuthorization(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("username " + username + " is not authorized");
        }
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Order " + orderId + " is not found")
        );
    }
}
