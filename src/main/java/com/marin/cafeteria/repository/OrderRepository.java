package com.marin.cafeteria.repository;

import com.marin.cafeteria.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
