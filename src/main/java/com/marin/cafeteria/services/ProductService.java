package com.marin.cafeteria.services;

import com.marin.cafeteria.dto.request.AdminProductDTO;
import com.marin.cafeteria.dto.response.ApiResponse;
import com.marin.cafeteria.model.OrderProduct;
import com.marin.cafeteria.model.Product;
import com.marin.cafeteria.model.ProductCategory;
import com.marin.cafeteria.repository.ProductCategoryRepository;
import com.marin.cafeteria.repository.ProductRepository;
import jdk.jfr.Category;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    //Returns the total price of a list of products.
    public BigDecimal calculateTotalPrice(List<OrderProduct> orderProducts) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.getProduct() == null) {
                throw new RuntimeException("Product cannot be null in OrderProduct");
            }

            // Fetch the full product from database
            Product product = productRepository.findById(orderProduct.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " +
                            orderProduct.getProduct().getId()));

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(orderProduct.getQuantity()));
            total = total.add(itemTotal);
        }

        return total;
    }


    public List<ProductCategory> getAllProducts() {
        return productCategoryRepository.findAll();
    }

    public void changeProduct(AdminProductDTO dto) {

        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getQuantityInStock() != 0) {
            product.setQuantityStock(dto.getQuantityInStock());
        }
        if (dto.getCategoryName() != null) {
            ProductCategory category = productCategoryRepository.findByName(dto.getCategoryName()).orElseThrow(() -> new RuntimeException("Category not found!"));
            product.setCategory(category);
        }
        productRepository.save(product);



    }

    public ApiResponse createProduct(AdminProductDTO dto) {
        Product product = new Product();

        if (dto.getName() == null || dto.getCategoryName().isEmpty() || dto.getPrice().compareTo(BigDecimal.ZERO) == 0)  {
            return new ApiResponse("FAILED", "Product object should have full information!");
        }

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        ProductCategory category = productCategoryRepository.findByName(dto.getCategoryName()).orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);
        product.setQuantityStock(dto.getQuantityInStock());
        productRepository.save(product);
        return new ApiResponse("SUCCESS", "Product created successfully!");
    }

}
