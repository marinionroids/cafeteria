package com.marin.cafeteria.core.services.product;

import com.marin.cafeteria.api.dto.request.AdminCategoryDTO;
import com.marin.cafeteria.api.dto.request.AdminProductCreateDTO;
import com.marin.cafeteria.api.dto.request.AdminProductDTO;
import com.marin.cafeteria.api.dto.response.ApiResponse;
import com.marin.cafeteria.api.dto.response.CategoryResponseDTO;
import com.marin.cafeteria.core.model.order.OrderProduct;
import com.marin.cafeteria.core.model.product.Product;
import com.marin.cafeteria.core.model.product.ProductCategory;
import com.marin.cafeteria.infrastructure.repository.product.ProductCategoryRepository;
import com.marin.cafeteria.infrastructure.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public ApiResponse getAllCategories() {
        List<ProductCategory> categories = productCategoryRepository.findAll();
        List<CategoryResponseDTO> categoryDTOs = new ArrayList<>();
        for (ProductCategory category : categories) {
            CategoryResponseDTO categoryDTO = new CategoryResponseDTO(category.getId(), category.getName());
            categoryDTOs.add(categoryDTO);

        }
        return new ApiResponse("Categories", categoryDTOs);
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

    public void updateProduct(AdminProductDTO dto) {

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

    public ApiResponse createProduct(AdminProductCreateDTO dto) {
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

    public ApiResponse createCategory(AdminCategoryDTO dto) {

        if (dto.getName() == null) {
         return new ApiResponse("FAILED", "Category object should have full information!");
        }
        ProductCategory category = new ProductCategory();
        category.setName(dto.getName());
        productCategoryRepository.save(category);
        return new ApiResponse("SUCCESS", "Category created successfully!");

    }

    public ApiResponse updateCategory(AdminCategoryDTO dto) {

        if (dto.getName() == null) {
            return new ApiResponse("FAILED", "Category object should have full information!");
        }
        ProductCategory category = productCategoryRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(dto.getName());
        productCategoryRepository.save(category);
        return new ApiResponse("SUCCESS", "Category updated successfully!");
    }

}
