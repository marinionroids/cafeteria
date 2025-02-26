package com.marin.cafeteria.infrastructure.repository.order;

import com.marin.cafeteria.core.model.order.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
