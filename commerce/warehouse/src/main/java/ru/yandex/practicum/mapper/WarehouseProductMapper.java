package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.request.NewProductInWarehouseRequest;

@Mapper(componentModel = "spring")
public interface WarehouseProductMapper {

    @Mapping(source = "request.dimension.width", target = "width")
    @Mapping(source = "request.dimension.height", target = "height")
    @Mapping(source = "request.dimension.depth", target = "depth")
    @Mapping(constant = "0", target = "quantity")
    WarehouseProduct mapToWarehouseProduct(NewProductInWarehouseRequest request);
}
