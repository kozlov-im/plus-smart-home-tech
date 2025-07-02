package ru.yandex.practicum.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProductQuantityRequest {

    @NotNull
    private UUID productId;

    @NotNull
    private Integer newQuantity;
}