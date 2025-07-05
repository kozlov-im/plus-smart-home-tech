package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/warehouse")
@Slf4j
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PutMapping
    public void addNewProductToWarehouse(@RequestBody NewProductInWarehouseRequest request) {
        log.info("addProductToWarehouse request {}", request);
        warehouseService.addNewProductToWarehouse(request);
    }

    @PostMapping("/add")
    public void addProductQuantity(@RequestBody AddProductToWarehouseRequest request) {
        log.info("addProductQuantity request {}", request);
        warehouseService.addProductQuantity(request);
    }

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        log.info("getWarehouseAddress request");
        return warehouseService.getWarehouseAddress();
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductsForBooking(@RequestBody ShoppingCartDto shoppingCartDto) {
        log.info("checkProductsForBooking request {}", shoppingCartDto);
        return warehouseService.checkProductsForBooking(shoppingCartDto);
    }
}
