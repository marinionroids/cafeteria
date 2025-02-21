package com.marin.cafeteria.services;

import com.marin.cafeteria.model.OrderProduct;
import com.marin.cafeteria.model.Product;
import com.marin.cafeteria.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
}
