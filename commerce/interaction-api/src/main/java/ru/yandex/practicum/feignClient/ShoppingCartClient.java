package ru.yandex.practicum.feignClient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.request.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient {

    @PutMapping
    ShoppingCartDto addProductToCart(@RequestParam String username,
                                     @RequestBody Map<UUID, Integer> products);

    @GetMapping
    ShoppingCartDto getUserShoppingCart(@RequestParam String username);

    @DeleteMapping
    void deactivateUserShoppingCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto deleteProductsFromShoppingCart(@RequestParam String username,
                                                   @RequestBody List<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantityInShoppingCart(@RequestParam String username,
                                                        @Valid @RequestBody ChangeProductQuantityRequest request);

    @GetMapping("/username")
    String getUsernameByShoppingCartId(@RequestParam UUID shoppingCartId);


}
