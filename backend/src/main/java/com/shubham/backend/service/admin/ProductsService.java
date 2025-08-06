package com.shubham.backend.service.admin;

import com.shubham.backend.body.ProductRequest;
import com.shubham.backend.body.ProductResponse;
import com.shubham.backend.entity.Product;
import com.shubham.backend.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service("adminProductsService")
public class ProductsService {

    @Autowired
    private ProductRepo productRepo;

    public List<ProductResponse> getAllProducts() {
        return productRepo.findAll().stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        // Ensure that the product name is not empty and price is positive
        if (productRequest.getName() == null || productRequest.getName().isEmpty() ||
                productRequest.getPrice() == null || productRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product name cannot be empty and price must be positive");
        }

        // Check if product with this name already exists
        if (productRepo.existsByName(productRequest.getName())) {
            throw new IllegalArgumentException("Product with this name already exists");
        }

        // Convert DTO to entity
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());

        // Set description (default to empty string if null)
        product.setDescription(productRequest.getDescription() != null ?
                productRequest.getDescription() : "");

        // Set imageUrl (default to empty string if null)
        product.setImageUrl(productRequest.getImageUrl() != null ?
                productRequest.getImageUrl() : "");

        // Save the product to the database
        Product savedProduct = productRepo.save(product);

        // Convert entity to response DTO
        return convertToProductResponse(savedProduct);
    }

    // Helper method to convert Product entity to ProductResponse DTO
    private ProductResponse convertToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        return response;
    }

    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        // Find the existing product
        try {
            Product existingProduct = productRepo.findById(productId).get();
            // Update fields
            if (productRequest.getName() != null && !productRequest.getName().isEmpty()) {
                existingProduct.setName(productRequest.getName());
            }
            if (productRequest.getDescription() != null) {
                existingProduct.setDescription(productRequest.getDescription());
            }
            if (productRequest.getPrice() != null && productRequest.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                existingProduct.setPrice(productRequest.getPrice());
            } else {
                throw new IllegalArgumentException("Price must be positive");
            }
            if (productRequest.getImageUrl() != null) {
                existingProduct.setImageUrl(productRequest.getImageUrl());
            }
            // Save the updated product
            Product updatedProduct = productRepo.save(existingProduct);

            // Convert entity to response DTO
            return convertToProductResponse(updatedProduct);
        } catch (Exception e) {
            throw new RuntimeException("having server error");
        }
    }

    public void deleteProduct(Long productId) {
        // Check if the product exists
        if (!productRepo.existsById(productId)) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }

        // Delete the product
        productRepo.deleteById(productId);
    }
}
