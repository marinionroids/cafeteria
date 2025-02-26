package com.marin.cafeteria.infrastructure.repository.product;

import com.marin.cafeteria.core.model.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findByName(String categoryName);
    Optional<ProductCategory> findById(int id);
}
