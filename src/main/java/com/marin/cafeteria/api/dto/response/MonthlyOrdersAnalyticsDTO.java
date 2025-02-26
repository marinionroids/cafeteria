package com.marin.cafeteria.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyOrdersAnalyticsDTO {

    private BigDecimal totalIncome;
    private int totalOrders;
    private BigDecimal averageOrderPrice;


}
