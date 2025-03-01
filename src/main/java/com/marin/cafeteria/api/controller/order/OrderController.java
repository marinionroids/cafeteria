package com.marin.cafeteria.api.controller.order;


import com.itextpdf.text.DocumentException;
import com.marin.cafeteria.api.dto.request.OrderRequestDTO;
import com.marin.cafeteria.api.dto.request.ReceiptRequestDTO;
import com.marin.cafeteria.api.dto.request.TimeDTO;
import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.infrastructure.repository.order.OrderRepository;
import com.marin.cafeteria.core.services.order.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderService orderService1, OrderRepository orderRepository) {
        this.orderService = orderService1;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/order")
    public ResponseEntity<?> newOrder(@RequestHeader("Authorization") String token, @RequestBody OrderRequestDTO orderRequestDTO) {

        try {
            ApiResponse response = orderService.createOrder(token, orderRequestDTO);

            HttpHeaders headers = orderService.getRecieptHeader();

            return new ResponseEntity<>(response.getData(), headers, HttpStatus.OK);
        }catch (DocumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/past-orders")
    public ResponseEntity<?> getPastOrders(@RequestHeader("Authorization") String token) {

        ApiResponse response = orderService.getPastOrders(token);

        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

    @PostMapping("/receipt")
    public ResponseEntity<?> newReceipt(@RequestBody ReceiptRequestDTO receiptRequestDTO) throws DocumentException {
        try {
            ApiResponse response = orderService.getRecieptOrder(receiptRequestDTO);
            HttpHeaders headers = orderService.getRecieptHeader();

            return new ResponseEntity<>(response.getData(), headers, HttpStatus.OK);

        }catch (DocumentException e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<?> getAdminOrders(@ModelAttribute TimeDTO timeDTO) {

        ApiResponse response = orderService.getAdminOrders(timeDTO);
        return new ResponseEntity<>(response.getData(), HttpStatus.OK);
    }

}
