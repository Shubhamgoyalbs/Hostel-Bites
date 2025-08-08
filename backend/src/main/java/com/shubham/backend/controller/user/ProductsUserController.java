package com.shubham.backend.controller.user;

import com.shubham.backend.body.ProductResponse;
import com.shubham.backend.service.user.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/products")
public class ProductsUserController {

    @Autowired
    @Qualifier("userProductsService")
    private ProductsService productsService;

    @GetMapping("/all")
    public List<ProductResponse> getAllProducts() {
        return productsService.getAllProducts();
    }

    @GetMapping("/search/{name}")
    public List<ProductResponse> searchProducts(@PathVariable String name) {
        return productsService.searchProducts(name.trim());
    }

}
