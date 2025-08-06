package com.shubham.backend.body;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String hostelName;
    private String phoneNo;
    private String roomNo;
    private String role = "USER"; // Default role is USER
}
