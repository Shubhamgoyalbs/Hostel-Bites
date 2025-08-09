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
        // Get products that are in seller's inventory (listed = true or have quantity > 0)
        List<UserProduct> users = userProductRepo.findAllByUser_UserIdAndListed(sellerId, true);
        return getProductResponses(users);
    }

    public List<ProductResponse> getAllNonListedProducts(Long sellerId) {
        // Get all products from the system
        List<Product> allProducts = productRepo.findAll();
        
        // Get only the products that seller has LISTED (listed = true)
        List<UserProduct> listedSellerProducts = userProductRepo.findAllByUser_UserIdAndListed(sellerId, true);
        
        // Get list of product IDs that seller has already LISTED
        List<Long> listedProductIds = listedSellerProducts.stream()
                .map(up -> up.getProduct().getProductId())
                .toList();
        
        // Filter products that seller hasn't listed yet
        List<Product> nonListedProducts = allProducts.stream()
                .filter(product -> !listedProductIds.contains(product.getProductId()))
                .toList();
        
        // Convert to ProductResponse (with quantity 0 since seller doesn't have them yet)
        return nonListedProducts.stream().map(product -> {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductId(product.getProductId());
            productResponse.setName(product.getName());
            productResponse.setPrice(product.getPrice());
            productResponse.setDescription(product.getDescription());
            productResponse.setImageUrl(product.getImageUrl());
            productResponse.setQuantity(0); // Seller doesn't have this product yet
            return productResponse;
        }).toList();
    }

    public String addProducts(long[] productIds, Long sellerId) {
        try {
            User seller = userRepo.findById(sellerId)
                    .orElseThrow(() -> new RuntimeException("Seller not found with id: " + sellerId));

            for (Long productId : productIds) {
                // Check if seller already has this product (regardless of listed status)
                var existingProduct = userProductRepo.findByUser_UserIdAndProduct_ProductId(sellerId, productId);
                
                if (existingProduct.isPresent()) {
                    // If product exists but is not listed, just mark it as listed
                    UserProduct existing = existingProduct.get();
                    existing.setListed(true);
                    userProductRepo.save(existing);
                } else {
                    // Create new UserProduct entry
                    UserProduct userProduct = new UserProduct();
                    Product product = productRepo.findById(productId)
                            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
                    userProduct.setUser(seller);
                    userProduct.setProduct(product);
                    userProduct.setQuantity(1); // Set default quantity to 1
                    userProduct.setListed(true); // Mark as listed when added
                    userProductRepo.save(userProduct);
                }
            }

            return "Products added successfully";
        } catch (Exception e) {
            return "Error in adding products,Please try again later";
        }
    }

    public String deleteProductFromSeller(Long productId, Long sellerId) {
        try {
            UserProduct userProduct = userProductRepo.findByUser_UserIdAndProduct_ProductId(sellerId, productId)
                    .orElseThrow(() -> new RuntimeException("Product not found for seller with id: " + sellerId));

            userProductRepo.delete(userProduct);
            return "Product deleted successfully";
        } catch (Exception e) {
            return "Error in deleting product, Please try again later";
        }
    }

    public String updateProduct(Integer updatedQuantity, Long sellerId, Long productId) {
        try {
            UserProduct userProduct = userProductRepo.findByUser_UserIdAndProduct_ProductId(sellerId, productId)
                    .orElseThrow(() -> new RuntimeException("Product not found for seller with id: " + sellerId));
            userProduct.setQuantity(updatedQuantity);
            userProductRepo.save(userProduct);
            return "Product updated successfully";
        } catch (Exception e) {
            return "Error in updating product, Please try again later";
        }
    }
}
