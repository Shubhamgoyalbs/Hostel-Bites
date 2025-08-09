package com.shubham.backend.controller.user;

import com.shubham.backend.body.OrderRequestBody;
import com.shubham.backend.body.OrderResponseBody;
import com.shubham.backend.service.user.OrderService;
import com.shubham.backend.service.JwtService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/user/order")
public class OrderController {

    private final OrderService orderService;
    private final JwtService jwtService;

    public OrderController(OrderService orderService, JwtService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestBody request, HttpServletRequest httpRequest) {
        if (request == null) {
            return ResponseEntity.badRequest().body("order must be valid");
        }

        try {
            // If userId is 0 or null (old token), extract it from JWT
            if (request.getUserId() == null || request.getUserId() == 0) {
                String authHeader = httpRequest.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7); // Remove "Bearer " prefix
                    Long userIdFromToken = jwtService.extractUserId(token);

                    if (userIdFromToken != null) {
                        request.setUserId(userIdFromToken);
                    } else {
                        // Try to get user from authentication context as fallback
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        if (auth != null && auth.getName() != null) {
                            // This is a fallback - you might need to implement user lookup by email
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Unable to identify user. Please login again with a fresh token.");
                        }
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing");
                }
            }

            String result = orderService.placeOrder(request);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Server error please try again later");
        }
    }

    @GetMapping("/allOrders/{userId}")
    public List<OrderResponseBody> allOrders(@PathVariable Long userId) {
        return orderService.allOrders(userId);
    }
}
