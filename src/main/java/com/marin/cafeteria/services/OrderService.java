package com.marin.cafeteria.services;

import com.itextpdf.text.DocumentException;
import com.marin.cafeteria.config.jwt.JwtUtil;
import com.marin.cafeteria.dto.request.OrderProductDTO;
import com.marin.cafeteria.dto.request.OrderRequestDTO;
import com.marin.cafeteria.dto.request.ReceiptRequestDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.model.Employee;
import com.marin.cafeteria.model.Order;
import com.marin.cafeteria.model.OrderProduct;
import com.marin.cafeteria.model.Product;
import com.marin.cafeteria.repository.EmployeeRepository;
import com.marin.cafeteria.repository.OrderRepository;
import com.marin.cafeteria.repository.ProductRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PDFSlipGenerator pdfSlipGenerator;
    private final ProductRepository productRepository;
    private final EmployeeService employeeService;
    private final JwtUtil jwtUtil;

    public OrderService(EmployeeRepository employeeRepository, OrderRepository orderRepository, ProductService productService, PDFSlipGenerator pdfSlipGenerator, ProductRepository productRepository, EmployeeService employeeService, JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.pdfSlipGenerator = pdfSlipGenerator;
        this.productRepository = productRepository;
        this.employeeService = employeeService;
        this.jwtUtil = jwtUtil;
    }

    public ApiResponse createOrder(String employeeToken, OrderRequestDTO orderRequestDTO) throws DocumentException {

        Order order = new Order();
        Employee employee = employeeRepository.findByUsername(jwtUtil.validateToken(employeeToken));
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



    public ApiResponse getPastOrders(String employeeToken){

        Employee employee = employeeRepository.findByUsername(jwtUtil.validateToken(employeeToken));

        List<Order> orders = orderRepository.findByServer(employee);

        return new ApiResponse("PAST_ORDERS", orders);



    }

    public HttpHeaders getRecieptHeader() {
        Random random = new Random();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "order-" + random.nextInt(10000) + ".pdf");

        return headers;

    }

}

