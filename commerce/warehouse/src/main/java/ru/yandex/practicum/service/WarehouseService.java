package ru.yandex.practicum.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.request.NewProductInWarehouseRequest;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void addNewProductToWarehouse(NewProductInWarehouseRequest request);

    void addProductQuantity(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();

    BookedProductsDto checkProductsForBooking(ShoppingCartDto shoppingCartDto, String type);

    void returnProduct(Map<UUID, Integer> products);
}
