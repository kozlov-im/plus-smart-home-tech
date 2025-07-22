package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.request.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.request.ShippedToDeliveryRequest;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/warehouse")
@Slf4j
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PutMapping
    public void addNewProductToWarehouse(@Valid @RequestBody NewProductInWarehouseRequest request) {
        log.info("addProductToWarehouse request {}", request);
        warehouseService.addNewProductToWarehouse(request);
    }

    @PostMapping("/add")
    public void addProductQuantity(@Valid @RequestBody AddProductToWarehouseRequest request) {
        log.info("addProductQuantity request {}", request);
        warehouseService.addProductQuantity(request);
    }

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        log.info("getWarehouseAddress request");
        return warehouseService.getWarehouseAddress();
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductsForBooking(@Valid @RequestBody ShoppingCartDto shoppingCartDto,
                                                     @RequestParam(defaultValue = "cart") String type) {
        log.info("checkProductsForBooking request {}", shoppingCartDto);
        return warehouseService.checkProductsForBooking(shoppingCartDto, type);
    }

    @PostMapping("/return")
    public void returnProduct(@RequestBody Map<UUID, Integer> products) {
        log.info("returnProduct request {}", products);
        warehouseService.returnProduct(products);
    }

    @PostMapping("/shipped")
    public void shippedToDelivery(@Valid @RequestBody ShippedToDeliveryRequest request) {
        log.info("shippedToDelivery request {}", request);
        warehouseService.shippedToDelivery(request);
    }

    @PostMapping("/assembly")
    public BookedProductsDto assemblyProductsForOrder(@Valid @RequestBody AssemblyProductsForOrderRequest request) {
        log.info("assemblyProductsForOrder request {}", request);
        return warehouseService.assemblyProductsForOrder(request);
    }


}
