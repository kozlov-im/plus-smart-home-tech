package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.feignClient.ShoppingCartClient;
import ru.yandex.practicum.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("checkstyle:Regexp")
@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartClient {

    private final ShoppingCartService shoppingCartService;

    @Override
    public ShoppingCartDto addProductToCart(@RequestParam String username,
                                            @RequestBody Map<UUID, Integer> products) {
        log.info("addProductToCart request username: {}, products: {}", username, products);
        return shoppingCartService.addProductToCard(username, products);
    }

    @Override
    public ShoppingCartDto getUserShoppingCart(@RequestParam String username) {
        log.info("getUserShoppingCart request for user {}", username);
        return shoppingCartService.getUserShoppingCart(username);
    }

    @Override
    public void deactivateUserShoppingCart(@RequestParam String username) {
        log.info("deactivateUserShoppingCart request for user {}", username);
        shoppingCartService.deactivateUserShoppingCart(username);
    }

    @Override
    public ShoppingCartDto deleteProductsFromShoppingCart(@RequestParam String username,
                                                          @RequestBody List<UUID> products) {
        log.info("deleteProductsFromShoppingCart request for user {} and products {}", username, products);
        return shoppingCartService.deleteProductsFromShoppingCart(username, products);
    }

    @Override
    public ShoppingCartDto changeProductQuantityInShoppingCart(@RequestParam String username,
                                                               @Valid @RequestBody ChangeProductQuantityRequest request) {
        log.info("changeProductQuantityInShoppingCart request for user {}, request {}", username, request);
        return shoppingCartService.changeProductQuantityInShoppingCart(username, request);
    }

    @Override
    public String getUsernameByShoppingCartId(@RequestParam UUID shoppingCartId) {
        log.info("getUsernameByShoppingCartId {}", shoppingCartId);
        return shoppingCartService.getUsernameByShoppingCartId(shoppingCartId);
    }

}
