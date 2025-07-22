package ru.yandex.practicum.feignClient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto createPayment(@Valid @RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    BigDecimal calculatePaymentTotalCost(@Valid @RequestBody OrderDto orderDto);

    @PostMapping("/refund")
    void setPaymentSuccess(@RequestBody UUID paymentId);

    @PostMapping("/productCost")
    BigDecimal calculatePaymentProductCost(@Valid @RequestBody OrderDto orderDto);

    @PostMapping("/failed")
    void setPaymentFailed(@RequestBody UUID paymentId);
}
