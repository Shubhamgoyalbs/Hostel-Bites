package com.shubham.backend.repository;

import com.shubham.backend.entity.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProductRepo extends JpaRepository<UserProduct, Long> {
    List<UserProduct> findAllByProduct_ProductId(Long productId);

    List<UserProduct> findAllByUser_UserId(Long userId);

    List<UserProduct> findAllByUser_UserIdAndListed(Long sellerId, boolean b);

    java.util.Optional<UserProduct> findByUser_UserIdAndProduct_ProductId(Long sellerId, Long productId);
}
