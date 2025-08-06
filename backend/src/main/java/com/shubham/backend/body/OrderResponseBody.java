package com.shubham.backend.body;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponseBody {
    private Long orderId;
    private int price;
    private SellerResponse seller = null;
    private SellerResponse user = null;
    private boolean isCompleted = false;
    private boolean isAccepted = false;

    private List<ORBProduct> products;
}
