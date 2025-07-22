package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@Valid @RequestBody OrderDto orderDto) {
        log.info("createPayment request for order {}", orderDto.getOrderId());
        return paymentService.createPayment(orderDto);
    }

    @PostMapping("/totalCost")
    public BigDecimal calculatePaymentTotalCost(@Valid @RequestBody OrderDto orderDto) {
        log.info("calculatePaymentTotalCost request for order {}", orderDto.getOrderId());
        return paymentService.calculatePaymentTotalCost(orderDto);
    }

    @PostMapping("/refund")
    public void setPaymentSuccess(@RequestBody UUID paymentId) {
        log.info("setPaymentSuccess request for payment {}", paymentId);
        paymentService.setPaymentSuccess(paymentId);
    }

    @PostMapping("/productCost")
    public BigDecimal calculatePaymentProductCost(@Valid @RequestBody OrderDto orderDto) {
        log.info("calculatePaymentProductCost request for order {}", orderDto.getOrderId());
        return paymentService.calculatePaymentProductCost(orderDto);
    }

    @PostMapping("/failed")
    public void setPaymentFailed(@RequestBody UUID paymentId) {
        log.info("setPaymentFailed request for payment {}", paymentId);
        paymentService.setPaymentFailed(paymentId);
    }


    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
