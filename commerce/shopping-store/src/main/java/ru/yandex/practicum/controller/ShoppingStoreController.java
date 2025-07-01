package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.repository.ShoppingStoreRepository;
import ru.yandex.practicum.service.ShoppingStoreService;

import java.util.Collection;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/shopping-store")
@Slf4j
public class ShoppingStoreController {
    private ShoppingStoreService shoppingStoreService;
    private ShoppingStoreRepository shoppingStoreRepository;

    @GetMapping
    public Collection<ProductDto> getProductsByCategory(@RequestParam ProductCategory category, Pageable pageable) {
        log.info("getProductsByCategory request category {}, pageable {}", category, pageable);
        return shoppingStoreService.getProductsByCategory(category, pageable);
    }

    @PutMapping
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("addProduct request {}", productDto);
        return shoppingStoreService.addProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("updateProduct request {}", productDto);
        return shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public void removeProductFromStore(@RequestBody UUID productId) {
        log.info("removeProductFromStore request {}", productId);
        shoppingStoreService.removeProductFromStore(productId);
    }

    @PostMapping("/quantityState")
    public ProductDto setProductQuantityState(UUID productId, QuantityState quantityState) {
        log.info("quantityState request: productId {}, quantityState {}", productId, quantityState);
        return shoppingStoreService.setProductQuantityState(productId, quantityState);

    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable UUID productId) {
        log.info("getProductById request {}", productId);
        return shoppingStoreService.getProductById(productId);
    }

}
