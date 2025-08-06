package com.shubham.backend.service.user;

import com.shubham.backend.body.ProductResponse;
import com.shubham.backend.body.SellerResponse;
import com.shubham.backend.entity.User;
import com.shubham.backend.entity.UserProduct;
import com.shubham.backend.repository.UserProductRepo;
import com.shubham.backend.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.shubham.backend.service.seller.ProductsSellerService.getProductResponses;

@Service
public class ProductsFromSellerService {
    private final UserRepo userRepo;
    private final UserProductRepo userProductRepo;

    public ProductsFromSellerService(UserRepo userRepo, UserProductRepo userProductRepo) {
        this.userRepo = userRepo;
        this.userProductRepo = userProductRepo;
    }

    public List<ProductResponse> getAllProductsFromSeller(Long sellerId) {
        List<UserProduct> userProducts = userProductRepo.findAllByUser_UserId(sellerId);
        return getProductResponses(userProducts);
    }

    public SellerResponse getSellerDetails(Long sellerId) {
        User user = userRepo.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));
        SellerResponse sellerResponse = new SellerResponse();
        sellerResponse.setUserId(user.getUserId());
        sellerResponse.setPhoneNo(user.getPhoneNo());
        sellerResponse.setEmail(user.getEmail());
        sellerResponse.setHostelName(user.getHostelName());
        sellerResponse.setRoomNumber(user.getRoomNumber());
        sellerResponse.setUsername(user.getUsername());
        return sellerResponse;
    }
}
