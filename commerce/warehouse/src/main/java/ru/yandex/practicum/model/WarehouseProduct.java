package ru.yandex.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseProduct {

    @Id
    private UUID productId;
    private boolean fragile;
    private double weight;
    private double width;
    private double height;
    private double depth;
    private int quantity;

}
