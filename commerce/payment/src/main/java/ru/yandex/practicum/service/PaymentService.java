package ru.yandex.practicum.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto orderDto);

    BigDecimal calculatePaymentTotalCost(OrderDto orderDto);

    void setPaymentSuccess(UUID paymentId);

    BigDecimal calculatePaymentProductCost(OrderDto orderDto);

    void setPaymentFailed(UUID paymentId);

}
