package com.marin.cafeteria.core.services.metrics;

import com.marin.cafeteria.infrastructure.config.jwt.JwtUtil;
import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.api.dto.response.MetricsResponse;
import com.marin.cafeteria.core.model.employee.Employee;
import com.marin.cafeteria.core.model.order.Order;
import com.marin.cafeteria.infrastructure.repository.employee.EmployeeRepository;
import com.marin.cafeteria.infrastructure.repository.order.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MetricsService {

    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    private final OrderRepository orderRepository;

    public MetricsService(EmployeeRepository employeeRepository, JwtUtil jwtUtil, OrderRepository orderRepository) {
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
        this.orderRepository = orderRepository;
    }

    public ApiResponse getMetrics(String employeeToken) {

        Employee employee = employeeRepository.findByUsername(jwtUtil.validateToken(employeeToken));
        MetricsResponse metricsResponse = getTotalDailyBalanceAndTotalOrders(employee);
        return new ApiResponse("Metrics", metricsResponse);


    }


    private MetricsResponse getTotalDailyBalanceAndTotalOrders(Employee employee) {


        BigDecimal total = BigDecimal.ZERO;
        LocalDateTime start = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        int orderCount = 0;
        List<Order> orders = orderRepository.findByOrderTimeBetweenAndServer(Timestamp.valueOf(start), Timestamp.valueOf(end), employee);
        for (Order order : orders) {
            total = total.add(order.getTotalPrice());
            orderCount++;
        }

        MetricsResponse metricsResponse = new MetricsResponse();
        metricsResponse.setTotal(total);
        metricsResponse.setOrderCount(orderCount);
        return metricsResponse;
    }
}
