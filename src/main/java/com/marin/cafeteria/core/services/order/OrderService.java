package com.marin.cafeteria.core.services.order;

import com.itextpdf.text.DocumentException;
import com.marin.cafeteria.api.dto.request.TimeDTO;
import com.marin.cafeteria.api.dto.response.AdminOrdersResponse;
import com.marin.cafeteria.infrastructure.config.jwt.JwtUtil;
import com.marin.cafeteria.api.dto.request.OrderProductDTO;
import com.marin.cafeteria.api.dto.request.OrderRequestDTO;
import com.marin.cafeteria.api.dto.request.ReceiptRequestDTO;
import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.core.model.employee.Employee;
import com.marin.cafeteria.core.model.order.Order;
import com.marin.cafeteria.core.model.order.OrderProduct;
import com.marin.cafeteria.core.model.product.Product;
import com.marin.cafeteria.core.services.product.ProductService;
import com.marin.cafeteria.infrastructure.repository.employee.EmployeeRepository;
import com.marin.cafeteria.infrastructure.repository.order.OrderRepository;
import com.marin.cafeteria.infrastructure.repository.product.ProductRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PDFSlipGenerator pdfSlipGenerator;
    private final ProductRepository productRepository;
    private final JwtUtil jwtUtil;

    public OrderService(EmployeeRepository employeeRepository, OrderRepository orderRepository, ProductService productService, PDFSlipGenerator pdfSlipGenerator, ProductRepository productRepository, JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.pdfSlipGenerator = pdfSlipGenerator;
        this.productRepository = productRepository;
        this.jwtUtil = jwtUtil;
    }

    // /api/order
    public ApiResponse createOrder(String employeeToken, OrderRequestDTO orderRequestDTO) throws DocumentException {

        Order order = new Order();
        Employee employee = employeeRepository
                .findByUsername(jwtUtil.validateToken(employeeToken));


        order.setServer(employee);

        List<OrderProduct> orderProducts = new ArrayList<>();

        for (OrderProductDTO productDTO : orderRequestDTO.getProducts()) {

            Product product = productRepository.findById(productDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setQuantity(productDTO.getQuantity());
            orderProduct.setOrder(order);

            orderProducts.add(orderProduct);
        }

        order.setOrderProducts(orderProducts);
        order.setTotalPrice(productService.calculateTotalPrice(orderProducts));
        order.setOrderTime(orderRequestDTO.getOrderTime());
        orderRepository.save(order);


        byte[] pdfBytes = pdfSlipGenerator.generateOrderPDF(order);


        return new ApiResponse("ORDER_CREATED", pdfBytes);
    }

    public ApiResponse getRecieptOrder(ReceiptRequestDTO receiptRequestDTO) throws DocumentException {

        Order order = orderRepository.findById(receiptRequestDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        byte[] pdfBytes = pdfSlipGenerator.generateOrderPDF(order);

        return new ApiResponse("RECEIPT_CREATED", pdfBytes);

    }



    // /api/past-orders
    public ApiResponse getPastOrders(String employeeToken){

        Employee employee = employeeRepository
                .findByUsername(jwtUtil.validateToken(employeeToken));
        List<Order> orders = orderRepository.findByServer(employee);
        return new ApiResponse("PAST_ORDERS", orders);

    }

    public ApiResponse getAdminOrders(TimeDTO timeDTO){
        LocalDateTime startDate = LocalDateTime.of(timeDTO.getYear(), timeDTO.getMonth(), timeDTO.getDay(), 0 , 0);
        LocalDateTime endDate = startDate.plusDays(1);
        List<Order> orders = orderRepository.findByOrderTimeBetween(Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
        List<AdminOrdersResponse> adminOrdersResponses = new ArrayList<>();
        for (Order order : orders) {
            AdminOrdersResponse adminOrdersResponse = new AdminOrdersResponse();
            adminOrdersResponse.setOrder(order);
            adminOrdersResponse.setEmployeeName(order.getServer().getUsername());
            adminOrdersResponse.setEmployeeId(order.getServer().getId());
            adminOrdersResponses.add(adminOrdersResponse);
        }

        return new ApiResponse("ADMIN_ORDERS", adminOrdersResponses);
    }
    public HttpHeaders getRecieptHeader() {
        Random random = new Random();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "order-" + random.nextInt(10000) + ".pdf");

        return headers;

    }



}

