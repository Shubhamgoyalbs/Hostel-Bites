package com.shubham.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hostel-Bites Backend Application
 * 
 * This is the main entry point for the Spring Boot application that serves as the backend
 * for the Hostel-Bites food ordering system. The application provides REST API endpoints
 * for managing users, products, orders, and authentication in a hostel environment.
 * 
 * Features:
 * - JWT-based authentication and authorization
 * - Role-based access control (USER, SELLER, ADMIN)
 * - Product catalog management
 * - Order processing and tracking
 * - User management with hostel-specific information
 */
@SpringBootApplication
public class BackendApplication {

    /**
     * Main method that starts the Spring Boot application
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
