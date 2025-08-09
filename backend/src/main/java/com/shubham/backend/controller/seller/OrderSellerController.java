package com.shubham.backend.controller.seller;

import com.shubham.backend.body.OrderResponseBody;
import com.shubham.backend.service.seller.OrderSellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller/order")
public class OrderSellerController {

    private final OrderSellerService orderSellerService;

    public OrderSellerController(OrderSellerService orderSellerService) {
        this.orderSellerService = orderSellerService;
    }

    @GetMapping("/allOrders/{sellerId}")
    public List<OrderResponseBody> getAllOrdersForSeller(@PathVariable Long sellerId) {
        return orderSellerService.getAllOrdersForSeller(sellerId);
    }

    @PutMapping("/accept/{orderId}")
    public ResponseEntity<String> acceptOrder(@PathVariable Long orderId) {
        try {
            boolean result = orderSellerService.acceptOrder(orderId);
            if (result) {
                return ResponseEntity.ok("Order accepted successfully.");
            } else {
                return ResponseEntity.badRequest().body("Order not found.");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while accepting the order.");
        }
    }

    @PutMapping("/complete/{orderId}")
    public ResponseEntity<String> completeOrder(@PathVariable Long orderId) {
        boolean updated = orderSellerService.completeOrder(orderId);
        if (updated) return ResponseEntity.ok("Order completed.");
        else return ResponseEntity.badRequest().body("Order not found or not accepted yet.");
    }
}
