package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.model.Booking;
import ru.yandex.practicum.request.AssemblyProductsForOrderRequest;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "fragile", source = "bookedProductsDto.fragile")
    @Mapping(target = "deliveryVolume", source = "bookedProductsDto.deliveryVolume")
    @Mapping(target = "deliveryWeight", source = "bookedProductsDto.deliveryWeight")
    @Mapping(target = "orderId", source = "request.orderId")
    @Mapping(target = "products", source = "request.products")
    Booking mapToBooking(BookedProductsDto bookedProductsDto, AssemblyProductsForOrderRequest request);

    BookedProductsDto mapToBookingDto(Booking booking);
}
