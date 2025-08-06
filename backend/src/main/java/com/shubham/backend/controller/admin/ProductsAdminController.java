package com.shubham.backend.controller.admin;

import com.shubham.backend.body.ProductRequest;
import com.shubham.backend.body.ProductResponse;
import com.shubham.backend.service.admin.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductsAdminController {

    private final ProductsService productsService;

    @Autowired
    public ProductsAdminController(@Qualifier("adminProductsService") ProductsService productsService) {
        this.productsService = productsService;
    }

    // Endpoint for getting all products
    @GetMapping("/all")
    public List<ProductResponse> getAllProducts() {
        return productsService.getAllProducts();
    }

    // Endpoint for creating a new product
    @PostMapping("/create")
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productsService.createProduct(productRequest);
    }

    // Endpoint for updating an existing product
    @PutMapping("/update/{productId}")
    public ProductResponse updateProduct(@PathVariable Long productId, @RequestBody ProductRequest productRequest) {
        return productsService.updateProduct(productId, productRequest);
    }

    // Endpoint for deleting a product
    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productsService.deleteProduct(productId);
    }
}
