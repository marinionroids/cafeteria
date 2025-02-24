package com.marin.cafeteria.services;

import com.marin.cafeteria.config.jwt.JwtUtil;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.dto.response.ReportsResponse;
import com.marin.cafeteria.model.Employee;
import com.marin.cafeteria.model.Order;
import com.marin.cafeteria.repository.EmployeeRepository;
import com.marin.cafeteria.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.Temporal;
import java.util.List;

@Service
public class ReportsService {

    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    private final OrderRepository orderRepository;

    public ReportsService(EmployeeRepository employeeRepository, JwtUtil jwtUtil, OrderRepository orderRepository) {
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
        this.orderRepository = orderRepository;
    }

    public ApiResponse getReports(String employeeToken) {

        Employee employee = employeeRepository.findByUsername(jwtUtil.validateToken(employeeToken));
        ReportsResponse reportsResponse = getTotalDailyBalanceAndTotalOrders(employee);
        return new ApiResponse("Reports", reportsResponse);


    }


    private ReportsResponse getTotalDailyBalanceAndTotalOrders(Employee employee) {


        BigDecimal total = BigDecimal.ZERO;
        LocalDateTime start = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        int orderCount = 0;
        List<Order> orders = orderRepository.findByOrderTimeBetweenAndServer(Timestamp.valueOf(start), Timestamp.valueOf(end), employee);
        for (Order order : orders) {
            total = total.add(order.getTotalPrice());
            orderCount++;
        }

        ReportsResponse reportsResponse = new ReportsResponse();
        reportsResponse.setTotal(total);
        reportsResponse.setOrderCount(orderCount);
        return reportsResponse;
    }
}
