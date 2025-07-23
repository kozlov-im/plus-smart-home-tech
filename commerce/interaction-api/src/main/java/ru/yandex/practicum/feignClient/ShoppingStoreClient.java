package ru.yandex.practicum.feignClient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.QuantityState;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @GetMapping
    Page<ProductDto> getProductsByCategory(@RequestParam ProductCategory category, Pageable pageable);

    @PutMapping
    ProductDto addProduct(@Valid @RequestBody ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@Valid @RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    void removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    ProductDto setProductQuantityState(@RequestParam UUID productId,
                                       @RequestParam QuantityState quantityState);

    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable UUID productId);
}
