package ru.yandex.practicum.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.DimensionDto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProductInWarehouseRequest {

    @NotNull
    UUID productId;

    boolean fragile;

    @NotNull
    DimensionDto dimension;

    @DecimalMin(value = "1")
    double weight;
}
