package com.marin.cafeteria.controller;


import com.itextpdf.text.DocumentException;
import com.marin.cafeteria.dto.request.OrderRequestDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.repository.OrderRepository;
import com.marin.cafeteria.services.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

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
    public ResponseEntity<?> newOrder(@RequestHeader("Authorization") String token, @RequestBody OrderRequestDTO orderRequestDTO) throws DocumentException {

        try {
            ApiResponse response = orderService.createOrder(token, orderRequestDTO);
            Random random = new Random();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "order-" + random.nextInt(10000) + ".pdf");

            return new ResponseEntity<>(response.getData(), headers, HttpStatus.OK);
        }catch (DocumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }
}
