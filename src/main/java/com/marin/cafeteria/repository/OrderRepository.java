package com.marin.cafeteria.repository;

import com.marin.cafeteria.model.Employee;
import com.marin.cafeteria.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByServer(Employee server);
    Optional<Order> findById(int orderId);
}
