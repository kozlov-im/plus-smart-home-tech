package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.request.CreateNewOrderRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "shoppingCartId", source = "request.shoppingCart.shoppingCartId")
    @Mapping(target = "products", source = "request.shoppingCart.products")
    @Mapping(target = "state", constant = "NEW")
    @Mapping(target = "deliveryWeight", source = "bookedProductsDto.deliveryWeight")
    @Mapping(target = "deliveryVolume", source = "bookedProductsDto.deliveryVolume")
    @Mapping(target = "fragile", source = "bookedProductsDto.fragile")
    Order mapToOrder(CreateNewOrderRequest request, BookedProductsDto bookedProductsDto);

    OrderDto mapToOrderDto(Order order);

    List<OrderDto> mapToListOrderDto(List<Order> orders);
}
