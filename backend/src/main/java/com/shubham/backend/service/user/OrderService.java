package com.shubham.backend.service.user;

import com.shubham.backend.body.ORBProduct;
import com.shubham.backend.body.OrderRequestBody;
import com.shubham.backend.body.OrderResponseBody;
import com.shubham.backend.body.SellerResponse;
import com.shubham.backend.entity.Order;
import com.shubham.backend.entity.OrderUs;
import com.shubham.backend.entity.User;
import com.shubham.backend.repository.OrderRepo;
import com.shubham.backend.repository.OrderUSRepo;
import com.shubham.backend.repository.ProductRepo;
import com.shubham.backend.repository.UserRepo;
import com.shubham.backend.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final UserRepo userRepo;
    private final OrderUSRepo orderUSRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final JwtService jwtService;

    public OrderService(UserRepo userRepo, OrderUSRepo orderUSRepo, ProductRepo productRepo, OrderRepo orderRepo, JwtService jwtService) {
        this.userRepo = userRepo;
        this.orderUSRepo = orderUSRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.jwtService = jwtService;
    }

    private static SellerResponse getSellerResponse(OrderUs orderUs) {
        SellerResponse seller = new SellerResponse();
        User sl = orderUs.getSeller();
        seller.setUserId(sl.getUserId());
        seller.setUsername(sl.getUsername());
        seller.setEmail(sl.getEmail());
        seller.setPhoneNo(sl.getPhoneNo());
        seller.setHostelName(sl.getHostelName());
        seller.setRoomNumber(sl.getRoomNumber());
        seller.setProfileImage(sl.getProfileImage());
        seller.setLocation(sl.getLocation());
        return seller;
    }

    public String placeOrder(OrderRequestBody request) {
        try {
            // Validate request data
            if (request.getUserId() == null || request.getSellerId() == null) {
                return "Invalid user or seller information";
            }
            
            if (request.getProductId() == null || request.getProductId().isEmpty()) {
                return "No products in order";
            }
            
            if (request.getQuantity() == null || request.getQuantity().size() != request.getProductId().size()) {
                return "Invalid product quantities";
            }
            
            // Check if user and seller exist
            if (!userRepo.existsById(request.getUserId())) {
                return "User not found";
            }
            
            if (!userRepo.existsById(request.getSellerId())) {
                return "Seller not found";
            }
            
            // Create the main order
            User user = userRepo.findById(request.getUserId()).get();
            User seller = userRepo.findById(request.getSellerId()).get();
            OrderUs orderUs = new OrderUs(user, seller, request.getPrice());
            OrderUs savedOrderUS = orderUSRepo.save(orderUs);

            // Add individual order items
            List<Long> productIds = request.getProductId();
            List<Integer> quantities = request.getQuantity();

            for (int i = 0; i < productIds.size(); i++) {
                Long productId = productIds.get(i);
                Integer quantity = quantities.get(i);
                
                // Validate product exists
                if (!productRepo.existsById(productId)) {
                    return "Product with ID " + productId + " not found";
                }
                
                if (quantity <= 0) {
                    return "Invalid quantity for product ID " + productId;
                }
                
                Order order = new Order();
                order.setOrder(savedOrderUS);
                order.setQuantity(quantity);
                order.setProduct(productRepo.findById(productId).get());
                orderRepo.save(order);
            }

            return "Order placed successfully";
        } catch (Exception e) {
            System.err.println("Error placing order: " + e.getMessage());
            e.printStackTrace();
            return "Server error please try again later";
        }
    }

    public List<OrderResponseBody> allOrders(Long userId) {
        try {
            List<OrderUs> orderUsList = orderUSRepo.findAllByUser_UserId(userId);
            List<OrderResponseBody> responseBodies = new ArrayList<>();
            for (OrderUs orderUs : orderUsList) {
                OrderResponseBody response = new OrderResponseBody();
                response.setOrderId(orderUs.getId());
                response.setPrice(orderUs.getPrice());
                SellerResponse seller = getSellerResponse(orderUs);

                List<Order> orders = orderRepo.findAllByOrder_Id(orderUs.getId());
                List<ORBProduct> products = new ArrayList<>();
                for (Order order : orders) {
                    products.add(new ORBProduct(order.getProduct().getName(), order.getQuantity(), order.getProduct().getPrice()));
                }

                response.setSeller(seller);
                response.setProducts(products);
                responseBodies.add(response);

            }
            return responseBodies;
        } catch (Exception e) {
            throw new RuntimeException("Server error please try again later");
        }
    }
}
