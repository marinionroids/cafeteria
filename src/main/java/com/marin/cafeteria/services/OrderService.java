package com.marin.cafeteria.services;

import com.itextpdf.text.DocumentException;
import com.marin.cafeteria.dto.request.OrderProductDTO;
import com.marin.cafeteria.dto.request.OrderRequestDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.model.Employee;
import com.marin.cafeteria.model.Order;
import com.marin.cafeteria.model.OrderProduct;
import com.marin.cafeteria.model.Product;
import com.marin.cafeteria.repository.EmployeeRepository;
import com.marin.cafeteria.repository.OrderRepository;
import com.marin.cafeteria.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {

    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PDFSlipGenerator pdfSlipGenerator;
    private final ProductRepository productRepository;

    public OrderService(EmployeeRepository employeeRepository, OrderRepository orderRepository, ProductService productService, PDFSlipGenerator pdfSlipGenerator, ProductRepository productRepository) {
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.pdfSlipGenerator = pdfSlipGenerator;
        this.productRepository = productRepository;
    }

    public ApiResponse createOrder(OrderRequestDTO orderRequestDTO) throws DocumentException {

        Order order = new Order();
        Employee employee = employeeRepository.findById(orderRequestDTO.getServerId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));        order.setServer(employee);
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


}

