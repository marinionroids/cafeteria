package com.marin.cafeteria.controller;


import com.marin.cafeteria.model.OrderProduct;
import com.marin.cafeteria.model.Product;
import com.marin.cafeteria.model.ProductCategory;
import com.marin.cafeteria.services.ProductService;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductCategory>> getAllProducts() {
        List<ProductCategory> products = productService.getOrderProducts();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);

    }
}
