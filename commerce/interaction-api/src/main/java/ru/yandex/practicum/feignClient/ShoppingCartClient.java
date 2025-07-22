package ru.yandex.practicum.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient {

    @GetMapping("/username")
    String getUsernameByShoppingCartId(@RequestParam UUID shoppingCartId);


}
