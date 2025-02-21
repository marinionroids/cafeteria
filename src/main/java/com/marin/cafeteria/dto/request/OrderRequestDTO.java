package com.marin.cafeteria.dto.request;


import com.marin.cafeteria.model.OrderProduct;
import com.marin.cafeteria.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private List<OrderProductDTO> products;
    private Timestamp orderTime;


}
