package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.enums.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @UuidGenerator
    UUID orderId;

    UUID shoppingCartId;

    String username;

    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Integer> products;

    UUID paymentId;
    UUID deliveryId;

    @Enumerated(EnumType.STRING)
    OrderState state;

    double deliveryWeight;
    double deliveryVolume;
    boolean fragile;
    BigDecimal totalPrice;
    BigDecimal deliveryPrice;
    BigDecimal productPrice;

}
