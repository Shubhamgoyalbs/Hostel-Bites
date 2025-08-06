package com.shubham.backend.service.seller;

import com.shubham.backend.body.ProductResponse;
import com.shubham.backend.entity.Product;
import com.shubham.backend.entity.User;
import com.shubham.backend.entity.UserProduct;
import com.shubham.backend.repository.ProductRepo;
import com.shubham.backend.repository.UserProductRepo;
import com.shubham.backend.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsSellerService {
    private final UserProductRepo userProductRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    public ProductsSellerService(UserProductRepo userProductRepo, UserRepo userRepo, ProductRepo productRepo) {
        this.userProductRepo = userProductRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    public static List<ProductResponse> getProductResponses(List<UserProduct> userProducts) {
        return userProducts.stream().map(userProduct -> {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductId(userProduct.getProduct().getProductId());
            productResponse.setName(userProduct.getProduct().getName());
            productResponse.setPrice(userProduct.getProduct().getPrice());
            productResponse.setDescription(userProduct.getProduct().getDescription());
            productResponse.setImageUrl(userProduct.getProduct().getImageUrl());
            productResponse.setQuantity(userProduct.getQuantity());
            return productResponse;
        }).toList();
    }

    public List<ProductResponse> getAllListedProducts(Long sellerId) {

        List<UserProduct> users = userProductRepo.findAllByUser_UserId(sellerId);

        return getProductResponses(users);
    }

    public List<ProductResponse> getAllNonListedProducts(Long sellerId) {
        List<UserProduct> users = userProductRepo.findAllByUser_UserIdAndListed(sellerId, false);

        return getProductResponses(users);
    }

    public String addProducts(long[] productIds, Long sellerId) {
        try {
            User seller = userRepo.findById(sellerId)
                    .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));

            for (Long productId : productIds) {
                UserProduct userProduct = new UserProduct();
                Product product = productRepo.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
                userProduct.setUser(seller);
                userProduct.setProduct(product);
                userProduct.setQuantity(0);
                userProductRepo.save(userProduct);
            }

            return "Products added successfully";
        } catch (Exception e) {
            return "Error in adding products,Please try again later";
        }
    }

    public String deleteProductFromSeller(Long productId, Long sellerId) {
        try {
            UserProduct userProduct = (UserProduct) userProductRepo.findByUser_UserIdAndProduct_ProductId(sellerId, productId)
                    .orElseThrow(() -> new RuntimeException("Product not found for seller with id: " + sellerId));

            userProductRepo.delete(userProduct);
            return "Product deleted successfully";
        } catch (Exception e) {
            return "Error in deleting product, Please try again later";
        }
    }

    public String updateProduct(Integer updatedQuantity, Long productId, Long sellerId) {
        try {
            UserProduct userProduct = (UserProduct) userProductRepo.findByUser_UserIdAndProduct_ProductId(sellerId, productId)
                    .orElseThrow(() -> new RuntimeException("Product not found for seller with id: " + sellerId));
            userProduct.setQuantity(updatedQuantity);
            userProductRepo.save(userProduct);
            return "Product updated successfully";
        } catch (Exception e) {
            return "Error in updating product, Please try again later";
        }
    }
}
