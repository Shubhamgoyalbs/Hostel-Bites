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
        List<OrderUs> orders = orderUSRepo.findAllByUser_UserId(sellerId);

        return orders.stream().map(orderUs -> {
            OrderResponseBody response = new OrderResponseBody();
            response.setOrderId(orderUs.getId());
            response.setPrice(orderUs.getPrice());
            response.setAccepted(Boolean.TRUE.equals(orderUs.getIsCompleted()));
            response.setCompleted(Boolean.TRUE.equals(orderUs.getIsAccepted()));

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
        OrderUs orderUs = orderUSRepo.findById(orderId).orElse(null);
        if (orderUs == null || Boolean.TRUE.equals(orderUs.getIsAccepted())) return false;
        orderUs.setIsAccepted(true);

        // Reduce quantity logic
        List<Order> orderItems = orderRepo.findAllByOrder_Id(orderId);
        for (Order orderItem : orderItems) {
            // Find the buyer's UserProduct row for this product
            UserProduct userProduct = (UserProduct) userProductRepo.findByUser_UserIdAndProduct_ProductId(
                    orderUs.getUser().getUserId(), orderItem.getProduct().getProductId()
            ).orElseThrow(() -> new RuntimeException("Product not found "));
            if (userProduct != null) {
                int currentQty = userProduct.getQuantity();
                int newQty = currentQty - orderItem.getQuantity();
                if (newQty < 0)
                    throw new IllegalStateException("Not enough quantity for product: " + orderItem.getProduct().getName());
                userProduct.setQuantity(newQty);
                userProductRepo.save(userProduct);
            }
        }

        orderUSRepo.save(orderUs);
        return true;
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
