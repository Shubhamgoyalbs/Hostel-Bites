package com.shubham.backend.service.user;

import com.shubham.backend.body.ProductResponse;
import com.shubham.backend.entity.Product;
import com.shubham.backend.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userProductsService")
public class ProductsService {

    @Autowired
    private ProductRepo productRepo;

    public List<ProductResponse> getAllProducts() {
        return productRepo.findAll().stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllProducts(); // Return all products if search term is empty
        }
        return productRepo.findAllByNameContainingIgnoreCase(name.trim()).stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse convertToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        return response;
    }
}
