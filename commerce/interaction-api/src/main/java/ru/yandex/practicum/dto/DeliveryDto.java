package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.enums.DeliveryState;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DeliveryDto {

    UUID deliveryId;

    @NotNull
    AddressDto fromAddress;

    @NotNull
    AddressDto toAddress;

    @NotNull
    UUID orderId;

    DeliveryState deliveryState;

}
