package com.marin.cafeteria.infrastructure.repository.order;

import com.marin.cafeteria.api.dto.response.MonthlyProductsAnalyticsDTO;
import com.marin.cafeteria.core.model.order.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;


@Repository

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query(value = "SELECT p.id as productId, p.name as productName, SUM(op.quantity) as totalQuantity " +
            "FROM order_products op " +
            "JOIN orders o ON op.order_id = o.id " +
            "JOIN products p ON op.product_id = p.id " +
            "WHERE o.order_time BETWEEN :startDate AND :endDate " +
            "GROUP BY p.id, p.name " +
            "ORDER BY totalQuantity DESC",
            nativeQuery = true)
    List<Object[]> findProductPurchaseCountsByDateRange(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate);
}

