package com.shubham.backend.repository;

import com.shubham.backend.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(@NotBlank @Email @Size(max = 100) String email);

    boolean existsByEmail(@NotBlank @Email @Size(max = 100) String email);

    List<User> findAllByUserId(Long sellerId);
}
