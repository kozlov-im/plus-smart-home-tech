package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.feignClient.WarehouseClient;
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
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final WarehouseClient warehouseClient;

    @PutMapping
    public ShoppingCartDto addProductToCart(@RequestParam String username,
                                            @RequestBody Map<UUID, Integer> products) {
        log.info("addProductToCart request username: {}, products: {}", username, products);
        return shoppingCartService.addProductToCard(username, products);
    }

    @GetMapping
    public ShoppingCartDto getUserShoppingCart(@RequestParam String username) {
        log.info("getUserShoppingCart request for user {}", username);
        return shoppingCartService.getUserShoppingCart(username);
    }

    @DeleteMapping
    public void deactivateUserShoppingCart(@RequestParam String username) {
        log.info("deactivateUserShoppingCart request for user {}", username);
        shoppingCartService.deactivateUserShoppingCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto deleteProductsFromShoppingCart(@RequestParam String username,
                                                          @RequestBody List<UUID> products) {
        log.info("deleteProductsFromShoppingCart request for user {} and products {}", username, products);
        return shoppingCartService.deleteProductsFromShoppingCart(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantityInShoppingCart(@RequestParam String username,
                                                               @RequestBody ChangeProductQuantityRequest request) {
        log.info("changeProductQuantityInShoppingCart request for user {}, request {}", username, request);
        return shoppingCartService.changeProductQuantityInShoppingCart(username, request);
    }

}
