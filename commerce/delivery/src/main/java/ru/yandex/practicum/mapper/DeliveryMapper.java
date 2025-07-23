package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.model.Delivery;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    Delivery mapToDelivery(DeliveryDto deliveryDto);

    DeliveryDto mapToDeliveryDto(Delivery delivery);
}
