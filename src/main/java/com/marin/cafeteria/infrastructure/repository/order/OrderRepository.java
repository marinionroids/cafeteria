package com.marin.cafeteria.infrastructure.repository.order;

import com.marin.cafeteria.core.model.employee.Employee;
import com.marin.cafeteria.core.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByServer(Employee server);
    Optional<Order> findById(int orderId);

    List<Order> findByOrderTimeBetweenAndServer(Timestamp orderTime, Timestamp orderTime2, Employee server);
    List<Order> findByOrderTimeBetween(Timestamp orderTime, Timestamp orderTime2);

}
