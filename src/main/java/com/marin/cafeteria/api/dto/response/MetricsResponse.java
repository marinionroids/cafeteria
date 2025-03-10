package com.marin.cafeteria.api.dto.response;

import com.marin.cafeteria.core.model.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricsResponse {

    private BigDecimal total;
    private int orderCount;
    private List<Order> orders;

}
