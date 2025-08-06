package com.shubham.backend.body;

import lombok.Data;

@Data
public class SellerResponse {
    private Long userId;
    private String username;
    private String email;
    private String phoneNo;
    private String hostelName;
    private String roomNumber;
    private String profileImage;
    private String location;
    private Integer quantity = 0;
}
