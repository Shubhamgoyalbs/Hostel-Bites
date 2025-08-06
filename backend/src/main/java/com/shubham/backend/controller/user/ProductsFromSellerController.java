package com.shubham.backend.controller.user;

import com.shubham.backend.body.ProductResponse;
import com.shubham.backend.body.SellerResponse;
import com.shubham.backend.service.user.ProductsFromSellerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seller/products")
public class ProductsFromSellerController {

    private final ProductsFromSellerService productsFromSellerService;

    public ProductsFromSellerController(ProductsFromSellerService productsFromSellerService) {
        this.productsFromSellerService = productsFromSellerService;
    }


    //    this method is used to get all products from a seller
    @GetMapping("/all/{sellerId}")
    public List<ProductResponse> getAllProductsFromSeller(@PathVariable Long sellerId) {
        return productsFromSellerService.getAllProductsFromSeller(sellerId);
    }

    //    this method is used to get the details of a seller for the seller page where
//    all products from the seller are displayed
    @GetMapping("/seller/{sellerId}")
    public SellerResponse getSellerDetails(@PathVariable Long sellerId) {
        return productsFromSellerService.getSellerDetails(sellerId);
    }
}
