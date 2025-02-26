package com.marin.cafeteria.api.controller.product;


import com.marin.cafeteria.api.dto.request.AdminCategoryDTO;
import com.marin.cafeteria.api.dto.request.AdminProductDTO;
import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.core.model.product.ProductCategory;
import com.marin.cafeteria.core.services.product.ProductService;
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
        productService.updateProduct(dto);
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

    @PostMapping("/admin/category")
    public ResponseEntity<?> addCategory(@RequestBody AdminCategoryDTO dto) {

        ApiResponse response = productService.createCategory(dto);
        if (response.getStatus().equals("FAILED")) {
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PutMapping("/admin/category")
    public ResponseEntity<?> updateCategory(@RequestBody AdminCategoryDTO dto) {
        ApiResponse response = productService.updateCategory(dto);
        if (response.getStatus().equals("FAILED")) {
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
