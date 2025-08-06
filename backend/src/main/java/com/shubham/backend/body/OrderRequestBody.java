package com.shubham.backend.body;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestBody {
    private long userId;
    private long sellerId;
    private SellerResponse sellerResponse;
    private List<Long> productId;
    private List<Integer> quantity;
    private int price;
}
