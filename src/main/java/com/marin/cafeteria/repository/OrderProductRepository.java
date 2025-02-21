package com.marin.cafeteria.repository;

import com.marin.cafeteria.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
