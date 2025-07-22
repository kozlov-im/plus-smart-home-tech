package ru.yandex.practicum.feignClient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.request.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient {

    @PostMapping("/check")
    BookedProductsDto checkProductsForBooking(@RequestBody ShoppingCartDto shoppingCartDto,
                                              @RequestParam(defaultValue = "cart") String type);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @PostMapping("/return")
    void returnProduct(@RequestBody Map<UUID, Integer> products);

    @PostMapping("/assembly")
    BookedProductsDto assemblyProductsForOrder(@RequestBody AssemblyProductsForOrderRequest request);

    @PostMapping("/shipped")
    void shippedToDelivery(@Valid @RequestBody ShippedToDeliveryRequest request);
}
