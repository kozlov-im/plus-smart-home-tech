package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.request.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto addProductToCard(String username, Map<UUID, Integer> products);

    ShoppingCartDto getUserShoppingCart(String username);

    void deactivateUserShoppingCart(String username);

    ShoppingCartDto deleteProductsFromShoppingCart(String username, List<UUID> products);

    ShoppingCartDto changeProductQuantityInShoppingCart(String username, ChangeProductQuantityRequest request);
}
