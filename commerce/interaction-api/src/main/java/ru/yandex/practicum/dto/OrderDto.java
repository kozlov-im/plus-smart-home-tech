package ru.yandex.practicum.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.enums.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {

    UUID orderId;
    UUID shoppingCartId;
    Map<UUID, Integer> products;
    UUID paymentId;
    UUID deliveryId;
    OrderState state;
    double deliveryWeight;
    double deliveryVolume;
    boolean fragile;
    BigDecimal totalPrice;
    BigDecimal deliveryPrice;
    BigDecimal productPrice;
}
