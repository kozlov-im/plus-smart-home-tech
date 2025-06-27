package ru.yandex.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dto.enums.ProductCategory;
import ru.yandex.practicum.model.Product;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {
@Query("SELECT p FROM Product p WHERE p.productCategory = :category")
    List<Product> findAllByCategory(ProductCategory category, PageRequest pageRequest);
}
