package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.enums.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {

    @Id
    @UuidGenerator
    UUID deliveryId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_address_id", referencedColumnName = "address_id")
    Address fromAddress;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_address_id", referencedColumnName = "address_id")
    Address toAddress;

    UUID orderId;

    @Enumerated(EnumType.STRING)
    DeliveryState deliveryState;
}
