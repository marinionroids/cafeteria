package com.marin.cafeteria.services;

import com.marin.cafeteria.model.OrderProduct;
import com.marin.cafeteria.model.Product;
import com.marin.cafeteria.model.ProductCategory;
import com.marin.cafeteria.repository.OrderProductRepository;
import com.marin.cafeteria.repository.ProductCategoryRepository;
import com.marin.cafeteria.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductService(ProductRepository productRepository, OrderProductRepository orderProductRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
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


    public List<ProductCategory> getOrderProducts() {
        return productCategoryRepository.findAll();
    }
}
