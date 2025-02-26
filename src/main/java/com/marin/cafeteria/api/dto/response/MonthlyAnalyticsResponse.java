package com.marin.cafeteria.api.dto.response;

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
public class MonthlyAnalyticsResponse {

    private int month;
    private int year;
    private BigDecimal totalIncome;
    private int totalOrders;
    private BigDecimal averageOrderPrice;
    private List<MonthlyServerAnalyticsDTO> monthlyServerAnalytics;
    private List<MonthlyProductsAnalyticsDTO> monthlyProductsAnalytics;

}
