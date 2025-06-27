package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.enums.ProductCategory;
import ru.yandex.practicum.dto.enums.QuantityState;

import java.util.Collection;
import java.util.UUID;

public interface ShoppingStoreService {

    Collection<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    ProductDto getProductById(UUID productId);

    void removeProductFromStore(UUID productId);

    ProductDto setProductQuantityState(UUID productId, QuantityState quantityState);
}
