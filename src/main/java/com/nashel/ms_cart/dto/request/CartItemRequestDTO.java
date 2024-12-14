package com.nashel.ms_cart.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class CartItemRequestDTO {
    @NotNull
    @Max(9999)
    @Min(1)
    private Long productId;

    @NotNull
    @Max(1000)
    @Min(1)
    private Integer itemsAmount;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getItemsAmount() {
        return itemsAmount;
    }

    public void setItemsAmount(int itemsAmount) {
        this.itemsAmount = itemsAmount;
    }
}
