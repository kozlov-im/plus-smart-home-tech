package ru.yandex.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.enums.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @UuidGenerator
    UUID paymentId;

    @NotNull
    UUID orderId;

    BigDecimal totalPayment;
    BigDecimal deliveryTotal;
    BigDecimal feeTotal;

    @Enumerated(EnumType.STRING)
    PaymentState paymentState;
}
