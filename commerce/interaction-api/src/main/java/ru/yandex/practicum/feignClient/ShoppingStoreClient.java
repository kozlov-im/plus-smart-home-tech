package ru.yandex.practicum.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.QuantityState;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @PostMapping("/quantityState")
    void updateProductQuantity(@RequestParam UUID productId, @RequestParam QuantityState quantityState);

    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable UUID productId);
}
