package com.shubham.backend.controller.seller;

import com.shubham.backend.body.ProductResponse;
import com.shubham.backend.service.seller.ProductsSellerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller/products")
public class ProductsSellerController {

    private final ProductsSellerService productsSellerService;

    public ProductsSellerController(ProductsSellerService productsSellerService) {
        this.productsSellerService = productsSellerService;
    }

    @GetMapping("/listedProducts/{sellerId}")
    public List<ProductResponse> getAllListedProducts(@PathVariable Long sellerId) {

        return productsSellerService.getAllListedProducts(sellerId);
    }

    @GetMapping("nonListedProducts/{sellerId}")
    public List<ProductResponse> getAllNonListedProducts(@PathVariable Long sellerId) {
        return productsSellerService.getAllNonListedProducts(sellerId);
    }

    @PostMapping("/addProducts/{sellerId}")
    public String addProducts(@RequestBody long[] productIds, @PathVariable Long sellerId) {
        return productsSellerService.addProducts(productIds, sellerId);
    }

    @DeleteMapping("/deleteProduct/{sellerId}/{productId}")
    public String deleteProduct(@PathVariable Long productId, @PathVariable Long sellerId) {
        return productsSellerService.deleteProductFromSeller(productId, sellerId);
    }

    @PutMapping("/updateProduct/{sellerId}/{productId}/{updatedQuantity}")
    public String updateProduct(@PathVariable Long sellerId, @PathVariable Long productId, @PathVariable Integer updatedQuantity) {
        return productsSellerService.updateProduct(updatedQuantity, sellerId, productId);
    }

}
