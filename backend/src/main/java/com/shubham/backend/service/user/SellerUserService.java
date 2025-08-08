package com.shubham.backend.service.user;

import com.shubham.backend.body.SellerResponse;
import com.shubham.backend.entity.User;
import com.shubham.backend.entity.UserProduct;
import com.shubham.backend.repository.ProductRepo;
import com.shubham.backend.repository.UserProductRepo;
import com.shubham.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerUserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserProductRepo userProductRepo;

    public List<SellerResponse> getAllSellersForProduct(Long productId) {
        return userProductRepo.findAllByProduct_ProductId(productId).stream()
                .map(this::convertToSellerResponse)
                .collect(Collectors.toList());
    }

    private SellerResponse convertToSellerResponse(UserProduct product) {
        SellerResponse sellerResponse = new SellerResponse();
        sellerResponse.setQuantity(product.getQuantity());
        User user = product.getUser();
        sellerResponse.setUserId(user.getUserId());
        sellerResponse.setUsername(user.getUsername());
        sellerResponse.setEmail(user.getEmail());
        sellerResponse.setHostelName(user.getHostelName());
        sellerResponse.setRoomNumber(user.getRoomNumber());
        sellerResponse.setLocation(user.getLocation());
        sellerResponse.setPhoneNo(user.getPhoneNo());
        sellerResponse.setProfileImage(user.getProfileImage());
        return sellerResponse;
    }

    public List<SellerResponse> searchSellersForProduct(Long productId, String username, Integer quantity) {
        List<SellerResponse> allSellers = getAllSellersForProduct(productId);
        if (username == null || username.trim().isEmpty()) {
            return allSellers; // Return all sellers if search term is empty
        }
        return allSellers.stream()
                .filter(seller -> seller.getUsername().toLowerCase().contains(username.toLowerCase()))
                .filter(seller -> seller.getQuantity() >= quantity)
                .collect(Collectors.toList());
    }
}
