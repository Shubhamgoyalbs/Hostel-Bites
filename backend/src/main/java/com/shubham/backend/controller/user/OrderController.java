package com.shubham.backend.controller.user;

import com.shubham.backend.body.OrderRequestBody;
import com.shubham.backend.body.OrderResponseBody;
import com.shubham.backend.service.user.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestBody OrderRequestBody request) {
        if (request == null) {
            return "order must be valid";
        }
        return orderService.placeOrder(request);
    }

    @GetMapping("/allOrders/{userId}")
    public List<OrderResponseBody> allOrders(@PathVariable Long userId) {
        return orderService.allOrders(userId);
    }
}
