package com.marin.cafeteria.api.dto.response;


import com.marin.cafeteria.core.model.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrdersResponse {

    private int employeeId;
    private String employeeName;
    private Order order;


}
