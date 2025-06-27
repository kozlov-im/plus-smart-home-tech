package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.dto.enums.ProductCategory;
import ru.yandex.practicum.dto.enums.ProductState;
import ru.yandex.practicum.dto.enums.QuantityState;

import java.util.UUID;

@Entity
@Table(name="products")
@Getter
@Setter
@ToString
public class Product {

    @Id
    @UuidGenerator
    private UUID productId;

    private String productName;
    private String description;
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    private QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    private double price;


}
