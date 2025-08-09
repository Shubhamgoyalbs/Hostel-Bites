package com.shubham.backend.service.seller;

import com.shubham.backend.body.ORBProduct;
import com.shubham.backend.body.OrderResponseBody;
import com.shubham.backend.body.SellerResponse;
import com.shubham.backend.entity.Order;
import com.shubham.backend.entity.OrderUs;
import com.shubham.backend.entity.Product;
import com.shubham.backend.entity.UserProduct;
import com.shubham.backend.repository.OrderRepo;
import com.shubham.backend.repository.OrderUSRepo;
import com.shubham.backend.repository.UserProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderSellerService {
    private final OrderUSRepo orderUSRepo;
    private final OrderRepo orderRepo;
    private final UserProductRepo userProductRepo;

    public OrderSellerService(OrderUSRepo orderUSRepo, OrderRepo orderRepo, UserProductRepo userProductRepo) {
        this.orderUSRepo = orderUSRepo;
        this.orderRepo = orderRepo;
        this.userProductRepo = userProductRepo;
    }

    private static SellerResponse getSellerResponse(OrderUs orderUs) {
        SellerResponse userResp = new SellerResponse();
        userResp.setUserId(orderUs.getUser().getUserId());
        userResp.setUsername(orderUs.getUser().getUsername());
        userResp.setEmail(orderUs.getUser().getEmail());
        userResp.setPhoneNo(orderUs.getUser().getPhoneNo());
        userResp.setHostelName(orderUs.getUser().getHostelName());
        userResp.setRoomNumber(orderUs.getUser().getRoomNumber());
        userResp.setProfileImage(orderUs.getUser().getProfileImage());
        userResp.setLocation(orderUs.getUser().getLocation());
        return userResp;
    }

    public List<OrderResponseBody> getAllOrdersForSeller(Long sellerId) {
        List<OrderUs> orders = orderUSRepo.findAllBySeller_UserId(sellerId);

        return orders.stream().map(orderUs -> {
            OrderResponseBody response = new OrderResponseBody();
            response.setOrderId(orderUs.getId());
            response.setPrice(orderUs.getPrice());

            boolean isAccepted = Boolean.TRUE.equals(orderUs.getIsAccepted());
            boolean isCompleted = Boolean.TRUE.equals(orderUs.getIsCompleted());

            System.out.println("Order ID: " + orderUs.getId() + ", isAccepted DB: " + orderUs.getIsAccepted() + ", mapped: " + isAccepted);

            response.setAccepted(isAccepted);
            response.setCompleted(isCompleted);

            SellerResponse userResp = getSellerResponse(orderUs);
            response.setUser(userResp);

            // Set product details
            List<Order> orderItems = orderRepo.findAllByOrder_Id(orderUs.getId());
            List<ORBProduct> products = orderItems.stream().map(orderItem -> {
                Product product = orderItem.getProduct();
                // Create ORBProduct: needs name, quantity, price
                // OR convert BigDecimal to int as in the ORBProduct constructor
                return new ORBProduct(
                        product.getName(),
                        orderItem.getQuantity(),
                        product.getPrice() // OR convert BigDecimal to int as in the ORBProduct constructor
                );
            }).collect(Collectors.toList());
            response.setProducts(products);

            return response;
        }).collect(Collectors.toList());
    }

    @Transactional
    public boolean acceptOrder(Long orderId) {
        try {
            System.out.println("Attempting to accept order with ID: " + orderId);

            OrderUs orderUs = orderUSRepo.findById(orderId).orElse(null);
            if (orderUs == null) {
                System.out.println("Order not found with ID: " + orderId);
                return false;
            }

            if (Boolean.TRUE.equals(orderUs.getIsAccepted())) {
                System.out.println("Order " + orderId + " is already accepted");
                return true; // Return true for idempotent operation
            }

            System.out.println("Order found - Seller ID: " + orderUs.getSeller().getUserId());
            orderUs.setIsAccepted(true);

            // Reduce quantity logic - reduce from SELLER's inventory, not buyer's
            List<Order> orderItems = orderRepo.findAllByOrder_Id(orderId);
            System.out.println("Found " + orderItems.size() + " order items");

            for (Order orderItem : orderItems) {
                System.out.println("Processing order item - Product ID: " + orderItem.getProduct().getProductId() + ", Quantity: " + orderItem.getQuantity());

                // Find the SELLER's UserProduct row for this product (seller's inventory)
                UserProduct sellerProduct = userProductRepo.findByUser_UserIdAndProduct_ProductId(
                        orderUs.getSeller().getUserId(), orderItem.getProduct().getProductId()
                ).orElseThrow(() -> new RuntimeException("Product not found in seller's inventory. Seller ID: " + orderUs.getSeller().getUserId() + ", Product ID: " + orderItem.getProduct().getProductId()));

                System.out.println("Seller has " + sellerProduct.getQuantity() + " units in inventory");

                int currentQty = sellerProduct.getQuantity();
                int newQty = currentQty - orderItem.getQuantity();
                if (newQty < 0) {
                    String errorMsg = "Not enough quantity for product: " + orderItem.getProduct().getName() + ". Available: " + currentQty + ", Requested: " + orderItem.getQuantity();
                    System.out.println(errorMsg);
                    throw new IllegalStateException(errorMsg);
                }
                sellerProduct.setQuantity(newQty);
                userProductRepo.save(sellerProduct);
                System.out.println("Updated inventory for product " + orderItem.getProduct().getName() + " from " + currentQty + " to " + newQty);
            }

            orderUSRepo.save(orderUs);
            System.out.println("Order " + orderId + " accepted successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Error accepting order " + orderId + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    @Transactional
    public boolean completeOrder(Long orderId) {
        OrderUs orderUs = orderUSRepo.findById(orderId).orElse(null);
        // Only completed if already accepted
        if (orderUs == null || !Boolean.TRUE.equals(orderUs.getIsAccepted()) || Boolean.TRUE.equals(orderUs.getIsCompleted()))
            return false;
        orderUs.setIsCompleted(true);
        orderUSRepo.save(orderUs);
        return true;
    }

}
