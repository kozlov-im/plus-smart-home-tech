package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.model.Product;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto mapToProductDto(Product product);

    Product mapToProduct(ProductDto productDto);

    Collection<ProductDto> mapToListProductDto(List<Product> products);

}
