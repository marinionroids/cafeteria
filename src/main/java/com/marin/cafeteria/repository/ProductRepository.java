package com.marin.cafeteria.repository;

import com.marin.cafeteria.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(int id);

}
