package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ShoppingStoreRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ProductMapper productMapper;
    private final ShoppingStoreRepository shoppingStoreRepository;

    @Override
    public Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable) {

        List<Sort.Order> sortOrderList =
                pageable.getSort() != null ? pageable.getSort().stream().map(Sort.Order::asc).toList() : null;

        PageRequest pageRequest = PageRequest.of(pageable.getPage(), pageable.getSize(),
                sortOrderList == null ? Sort.unsorted() : Sort.by(sortOrderList));

        Page<Product> products = shoppingStoreRepository.findAllByCategory(category, pageRequest);

        return products.map(product -> new ProductDto(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getImageSrc(),
                product.getQuantityState(),
                product.getProductState(),
                product.getProductCategory(),
                product.getPrice()));
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.mapToProduct(productDto);
        Product savedProduct = shoppingStoreRepository.save(product);
        return productMapper.mapToProductDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        getProduct(productDto.getProductId());
        Product product = productMapper.mapToProduct(productDto);
        return productMapper.mapToProductDto(shoppingStoreRepository.save(product));
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        Product product = getProduct(productId);
        return productMapper.mapToProductDto(product);
    }

    @Override
    public void removeProductFromStore(UUID productId) {
        Product product = getProduct(productId);
        product.setProductState(ProductState.DEACTIVATE);
        shoppingStoreRepository.save(product);
    }

    @Override
    public ProductDto setProductQuantityState(UUID productId, QuantityState quantityState) {
        Product product = getProduct(productId);
        product.setQuantityState(quantityState);
        return productMapper.mapToProductDto(shoppingStoreRepository.save(product));
    }

    private Product getProduct(UUID id) {
        return shoppingStoreRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product is not found by id"));
    }

}
