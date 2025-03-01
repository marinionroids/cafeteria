package com.marin.cafeteria.core.services.metrics;

import com.marin.cafeteria.api.dto.response.*;
import com.marin.cafeteria.core.model.employee.Employee;
import com.marin.cafeteria.core.model.order.Order;
import com.marin.cafeteria.core.model.product.Product;
import com.marin.cafeteria.infrastructure.repository.employee.EmployeeRepository;
import com.marin.cafeteria.infrastructure.repository.order.OrderProductRepository;
import com.marin.cafeteria.infrastructure.repository.order.OrderRepository;
import com.marin.cafeteria.infrastructure.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonthlyAnalyticsService {


    private final OrderRepository orderRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    public MonthlyAnalyticsService(OrderRepository orderRepository, EmployeeRepository employeeRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
    }

    // General function to calculate analytics based on a list of orders.
    private MonthlyOrdersAnalyticsDTO calculateOrderAnalytics(List<Order> monthlyOrders) {

        BigDecimal totalIncome = BigDecimal.ZERO;
        // Gets the size of the list of orders for the month.
        int totalOrders = monthlyOrders.size();

        BigDecimal averageOrderPrice;

        // Loops through the orders and adds their prices to totalIncome.
        for (Order order : monthlyOrders) {
            totalIncome = totalIncome.add(order.getTotalPrice());
        }

        // Gets the average order price.
        averageOrderPrice = totalIncome.divide(BigDecimal.valueOf(totalOrders), RoundingMode.HALF_DOWN);

        return new MonthlyOrdersAnalyticsDTO(totalIncome, totalOrders, averageOrderPrice);

    }

    private MonthlyOrdersAnalyticsDTO getMonthlytotalIncomeForSpecificServer(int month, int year, Employee employee) {

        // Gets the first day of the month requested
        LocalDateTime firstOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        // Gets the list of orders for the specified month.
        List<Order> monthlyOrders = orderRepository
                .findByOrderTimeBetweenAndServer(Timestamp.valueOf(firstOfMonth), Timestamp.valueOf(firstOfMonth.plusMonths(1)), employee);

        if (monthlyOrders.isEmpty()) {
            return new MonthlyOrdersAnalyticsDTO(BigDecimal.ZERO, 0, BigDecimal.ZERO);
        }

        return calculateOrderAnalytics(monthlyOrders);


    }

    private MonthlyOrdersAnalyticsDTO getMonthlytotalIncome(int month, int year) {

        // Gets the first day of the month requested
        LocalDateTime firstOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        // Gets the list of orders for the specified month.
        List<Order> monthlyOrders = orderRepository
                .findByOrderTimeBetween(Timestamp.valueOf(firstOfMonth), Timestamp.valueOf(firstOfMonth.plusMonths(1)));

        if (monthlyOrders.isEmpty()) {
            return null;
        }

        return calculateOrderAnalytics(monthlyOrders);


    }

    private List<MonthlyServerAnalyticsDTO> getMonthlyServerAnalytics(int month, int year) {

        List<Employee> employees = employeeRepository.findAll();

        // List of statistics for all the servers.
        List<MonthlyServerAnalyticsDTO> monthlyServerAnalytics = new ArrayList<>();


        // Loops through every employee
        for (Employee employee : employees) {
            // Creates a Object that has totalIncome, totalOrders and orderAverage. (The one we use for the orders as well. we can use it for employees as well.)
            MonthlyServerAnalyticsDTO employeeAnalyticsDTO = new MonthlyServerAnalyticsDTO();
            employeeAnalyticsDTO.setEmployeeId(employee.getId());
            employeeAnalyticsDTO.setEmployeeName(employee.getUsername());

            MonthlyOrdersAnalyticsDTO ordersAnalyticsDTO = getMonthlytotalIncomeForSpecificServer(month, year, employee);

            employeeAnalyticsDTO.setTotalIncome(ordersAnalyticsDTO.getTotalIncome());
            employeeAnalyticsDTO.setTotalOrders(ordersAnalyticsDTO.getTotalOrders());
            employeeAnalyticsDTO.setAverageOrderPrice(ordersAnalyticsDTO.getAverageOrderPrice());

            // Adds it to the list.
            monthlyServerAnalytics.add(employeeAnalyticsDTO);
        }

        return monthlyServerAnalytics;

    }

    private List<MonthlyProductsAnalyticsDTO> getMonthlyProductAnalytics(int month, int year) {
        LocalDateTime firstOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        List<Object[]> info = orderProductRepository
                .findProductPurchaseCountsByDateRange(Timestamp.valueOf(firstOfMonth), Timestamp.valueOf(firstOfMonth.plusMonths(1)));

        List<MonthlyProductsAnalyticsDTO> monthlyProductsAnalytics = new ArrayList<>();

        for (Object[] row : info) {
            MonthlyProductsAnalyticsDTO productAnalyticsDTO = new MonthlyProductsAnalyticsDTO();
            productAnalyticsDTO.setProductName((String) row[1]);
            BigDecimal productSoldQuantity = (BigDecimal) row[2];
            productAnalyticsDTO.setSoldQuantity(productSoldQuantity.intValue());
            monthlyProductsAnalytics.add(productAnalyticsDTO);
        }

        return monthlyProductsAnalytics;
    }


    public ApiResponse getMonthlyAnalytics(int month, int year) {

        // Basically returns totalIncome, totalOrders, averageOrderPrice.
        MonthlyOrdersAnalyticsDTO monthlyOrdersAnalyticsDTO = getMonthlytotalIncome(month, year);

        if (monthlyOrdersAnalyticsDTO == null) {
            return new ApiResponse("NO_ORDERS", "There are no orders to show.");
        }

        List<MonthlyServerAnalyticsDTO> monthlyServerAnalytics = getMonthlyServerAnalytics(month, year);

        List<MonthlyProductsAnalyticsDTO> monthlyProductsAnalytics = getMonthlyProductAnalytics(month, year);



        MonthlyAnalyticsResponse monthlyAnalyticsResponse = new MonthlyAnalyticsResponse();
        monthlyAnalyticsResponse.setYear(year);
        monthlyAnalyticsResponse.setMonth(month);
        monthlyAnalyticsResponse.setTotalIncome(monthlyOrdersAnalyticsDTO.getTotalIncome());
        monthlyAnalyticsResponse.setTotalOrders(monthlyOrdersAnalyticsDTO.getTotalOrders());
        monthlyAnalyticsResponse.setAverageOrderPrice(monthlyOrdersAnalyticsDTO.getAverageOrderPrice());
        monthlyAnalyticsResponse.setMonthlyServerAnalytics(monthlyServerAnalytics);
        monthlyAnalyticsResponse.setMonthlyProductsAnalytics(monthlyProductsAnalytics);

        return new ApiResponse("SUCCESS", monthlyAnalyticsResponse);


    }
}
