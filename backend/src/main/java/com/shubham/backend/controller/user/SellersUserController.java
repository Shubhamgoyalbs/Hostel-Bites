package com.shubham.backend.controller.user;

import com.shubham.backend.body.SellerResponse;
import com.shubham.backend.service.user.SellerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class SellersUserController {

    @Autowired
    private SellerUserService sellerUserService;

    @GetMapping("/sellers/{productId}")
    public List<SellerResponse> getAllSellersForProduct(@PathVariable Long productId) {
        return sellerUserService.getAllSellersForProduct(productId);
    }

    @GetMapping("sellers/{productId}/search/{username}/quantity/{quantity}")
    public List<SellerResponse> searchSellersForProduct(@PathVariable Long productId, @PathVariable String username, @PathVariable Integer quantity) {
        return sellerUserService.searchSellersForProduct(productId, username, quantity);
    }
}
