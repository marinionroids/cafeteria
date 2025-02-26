package com.marin.cafeteria.api.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductDTO {

    private int id;

    private String name;
    private BigDecimal price = BigDecimal.ZERO;
    private int quantityInStock;
    private String categoryName;

}
