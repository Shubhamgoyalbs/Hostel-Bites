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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final UserRepo userRepo;
    private final OrderUSRepo orderUSRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public OrderService(UserRepo userRepo, OrderUSRepo orderUSRepo, ProductRepo productRepo, OrderRepo orderRepo) {
        this.userRepo = userRepo;
        this.orderUSRepo = orderUSRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
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
            OrderUs orderUs = new OrderUs(userRepo.findById(request.getUserId()).get(), userRepo.findById(request.getSellerId()).get(), request.getPrice());
            orderUSRepo.save(orderUs);

            OrderUs savedOrderUS = orderUSRepo.findByUser_UserIdAndSeller_UserId(request.getUserId(), request.getSellerId());

            List<Long> productId = request.getProductId();
            List<Integer> quantity = request.getQuantity();

            for (int i = 0; i < productId.size(); i++) {
                Order order = new Order();
                order.setOrder(savedOrderUS);
                order.setQuantity(quantity.get(i));
                order.setProduct(productRepo.findById(productId.get(i)).get());
            }

            return "Order placed successfully";
        } catch (Exception e) {
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
