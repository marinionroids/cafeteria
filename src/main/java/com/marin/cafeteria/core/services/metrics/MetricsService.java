package com.marin.cafeteria.core.services.metrics;

import com.marin.cafeteria.api.dto.response.DailyServerBalance;
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
import java.util.ArrayList;
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


    public ApiResponse getMetrics(int day, int month, int year, String employeeToken) {

        Employee employee = employeeRepository.findByUsername(jwtUtil.validateToken(employeeToken));
        MetricsResponse metricsResponse = getTotalDailyBalanceAndTotalOrders(day, month, year, employee);
        return new ApiResponse("Metrics", metricsResponse);


    }


    private MetricsResponse getTotalDailyBalanceAndTotalOrders(int day, int month, int year, Employee employee) {


        BigDecimal total = BigDecimal.ZERO;
        LocalDateTime start = LocalDateTime.of(year, month, day, 0, 0);
        LocalDateTime end = start.with(LocalTime.MAX);
        int orderCount = 0;
        List<Order> orders = orderRepository.findByOrderTimeBetweenAndServer(Timestamp.valueOf(start), Timestamp.valueOf(end), employee);
        for (Order order : orders) {
            total = total.add(order.getTotalPrice());
            orderCount++;
        }

        MetricsResponse metricsResponse = new MetricsResponse();
        metricsResponse.setTotal(total);
        metricsResponse.setOrderCount(orderCount);
        metricsResponse.setOrders(orders);
        return metricsResponse;
    }

    public ApiResponse getAllServersBalance(int day, int month, int year) {

        List<Employee> employees = employeeRepository.findAll();
        List<DailyServerBalance> dailyServerBalances = new ArrayList<>();

        for (Employee employee : employees) {
            DailyServerBalance dailyServerBalance = new DailyServerBalance();
            MetricsResponse metricsResponse = getTotalDailyBalanceAndTotalOrders(day, month, year, employee);
            dailyServerBalance.setEmployeeId(employee.getId());
            dailyServerBalance.setDailyOrders(metricsResponse.getOrders());
            dailyServerBalance.setTotalDailyIncome(metricsResponse.getTotal());
            dailyServerBalances.add(dailyServerBalance);
        }
        return new ApiResponse("Servers Balance", dailyServerBalances);

    }
}
