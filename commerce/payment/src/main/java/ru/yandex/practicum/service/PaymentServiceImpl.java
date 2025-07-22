package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.PaymentState;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.PaymentNotFoundException;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.feignClient.OrderClient;
import ru.yandex.practicum.feignClient.ShoppingStoreClient;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        checkOrder(orderDto);
        checkPaymentInfo(orderDto.getTotalPrice(), orderDto.getDeliveryPrice(), orderDto.getProductPrice());
        Payment payment = paymentMapper.mapToPayment(orderDto);
        return paymentMapper.mapToPaymentDto(paymentRepository.save(payment));
    }

    @Override
    public BigDecimal calculatePaymentTotalCost(OrderDto orderDto) {
        checkOrder(orderDto);
        checkPaymentInfo(orderDto.getDeliveryPrice(), orderDto.getProductPrice());
        return orderDto.getProductPrice().add(orderDto.getDeliveryPrice());
    }

    @Override
    public void setPaymentSuccess(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setPaymentState(PaymentState.SUCCESS);
        orderClient.setOrderPaymentSuccess(payment.getOrderId());
        paymentRepository.save(payment);
    }

    @Override
    public BigDecimal calculatePaymentProductCost(OrderDto orderDto) {
        checkOrder(orderDto);
        Map<UUID, Integer> products = orderDto.getProducts();
        return products.entrySet().stream().map(
                entry -> {
                    ProductDto product = shoppingStoreClient.getProductById(entry.getKey());
                    return product.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void setPaymentFailed(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setPaymentState(PaymentState.FAILED);
        orderClient.setOrderPaymentFailed(payment.getOrderId());
        paymentRepository.save(payment);
    }

    private void checkOrder(OrderDto orderDto) {
        OrderDto orderForChecking = orderClient.getOrderById(orderDto.getOrderId());
        if (!orderForChecking.getShoppingCartId().equals(orderDto.getShoppingCartId())) {
            throw new NotFoundException("shopping cart " + orderDto.getShoppingCartId() + " is not found");
        }
        Map<UUID, Integer> productsForChecking = orderForChecking.getProducts();
        Map<UUID, Integer> products = orderDto.getProducts();

        products.forEach((key, value) -> {
            if (!productsForChecking.containsKey(key)) {
                throw new ProductNotFoundException("product " + key + " is not found");
            }
            if (!productsForChecking.containsValue(value)) {
                throw new NotFoundException("product " + key + " has incorrect quantity");
            }
        });
    }

    private void checkPaymentInfo(BigDecimal... prices) {
        for (BigDecimal price : prices) {
            Optional.ofNullable(price).filter(p -> p.compareTo(BigDecimal.ZERO) > 0)
                    .orElseThrow(() -> new NotEnoughInfoInOrderToCalculateException("Not enough info in order for calculating"));
        }
    }

    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment " + paymentId + " is not found")
        );
    }


}