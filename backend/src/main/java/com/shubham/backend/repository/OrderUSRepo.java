package com.shubham.backend.repository;

import com.shubham.backend.entity.OrderUs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderUSRepo extends JpaRepository<OrderUs, Long> {
    OrderUs findByUser_UserIdAndSeller_UserId(Long userUserId, Long sellerUserId);

    List<OrderUs> findAllByUser_UserId(Long userUserId);
}
