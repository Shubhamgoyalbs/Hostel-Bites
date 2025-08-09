package com.shubham.backend.body;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestBody {
    private Long userId;
    private Long sellerId;
    private SellerResponse sellerResponse;
    private List<Long> productId;
    private List<Integer> quantity;
    private int price;
}
