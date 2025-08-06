package com.shubham.backend.repository;

import com.shubham.backend.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    boolean existsByName(@NotBlank @Size(max = 100) String name);

    List<Product> findAllByNameContainingIgnoreCase(@NotBlank @Size(max = 100) String name);

    // Additional query methods can be defined here if needed
}
