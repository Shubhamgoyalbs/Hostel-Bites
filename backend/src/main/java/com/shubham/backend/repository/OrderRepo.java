package com.shubham.backend.repository;

import com.shubham.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findAllByOrder_Id(Long orderId);
}
