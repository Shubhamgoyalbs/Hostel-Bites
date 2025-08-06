package com.shubham.backend.body;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ORBProduct {

    private String productName;
    private int quantity;
    private int price;

    public ORBProduct(@Size(max = 100) @NotNull String name, @NotNull Integer quantity, @NotNull BigDecimal price) {
        this.productName = name;
        this.quantity = quantity;
        this.price = price.intValue();
    }
}
