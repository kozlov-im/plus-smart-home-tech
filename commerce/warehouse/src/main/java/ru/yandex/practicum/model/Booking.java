package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @UuidGenerator
    @Column(name = "booking_id")
    UUID bookingId;

    boolean fragile;
    double deliveryVolume;
    double deliveryWeight;

    @Column(name = "delivery_id")
    UUID deliveryId;

    @Column(name = "order_id")
    UUID orderId;

    @ElementCollection
    @CollectionTable(name = "booking_product", joinColumns = @JoinColumn(name = "booking_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Integer> products;

}
