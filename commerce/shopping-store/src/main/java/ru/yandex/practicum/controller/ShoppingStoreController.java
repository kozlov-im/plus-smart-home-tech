package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.feignClient.ShoppingStoreClient;
import ru.yandex.practicum.service.ShoppingStoreService;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/shopping-store")
@Slf4j
public class ShoppingStoreController implements ShoppingStoreClient {
    private ShoppingStoreService shoppingStoreService;

    @Override
    public Page<ProductDto> getProductsByCategory(@RequestParam ProductCategory category, Pageable pageable) {
        log.info("getProductsByCategory request category {}, pageable {}", category, pageable);
        return shoppingStoreService.getProductsByCategory(category, pageable);
    }

    @Override
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("addProduct request {}", productDto);
        return shoppingStoreService.addProduct(productDto);
    }

    @Override
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("updateProduct request {}", productDto);
        return shoppingStoreService.updateProduct(productDto);
    }

    @Override
    public void removeProductFromStore(@RequestBody UUID productId) {
        log.info("removeProductFromStore request {}", productId);
        shoppingStoreService.removeProductFromStore(productId);
    }

    @Override
    public ProductDto setProductQuantityState(@RequestParam UUID productId,
                                              @RequestParam QuantityState quantityState) {
        log.info("quantityState request: productId {}, quantityState {}", productId, quantityState);
        return shoppingStoreService.setProductQuantityState(productId, quantityState);

    }

    @Override
    public ProductDto getProductById(@PathVariable UUID productId) {
        log.info("getProductById request {}", productId);
        return shoppingStoreService.getProductById(productId);
    }

}
