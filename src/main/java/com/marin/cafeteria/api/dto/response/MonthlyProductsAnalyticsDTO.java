package com.marin.cafeteria.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyProductsAnalyticsDTO {

    private String productName;
    private int soldQuantity;

}
