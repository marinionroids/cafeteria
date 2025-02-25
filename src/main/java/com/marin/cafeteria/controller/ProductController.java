package com.marin.cafeteria.controller;


import com.marin.cafeteria.dto.request.AdminProductDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.model.ProductCategory;
import com.marin.cafeteria.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        List<ProductCategory> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);

    }



    @PutMapping("/admin/product")
    public ResponseEntity<?> changeProduct(@RequestBody AdminProductDTO dto) {
        productService.changeProduct(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/admin/product")
    public ResponseEntity<?> addProduct(@RequestBody AdminProductDTO dto) {
        ApiResponse response = productService.createProduct(dto);
        if (response.getStatus().equals("FAILED")) {
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


}
