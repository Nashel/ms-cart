package com.nashel.ms_cart.dto.response;

import com.nashel.ms_cart.dto.common.ProductDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CartItemResponseDTO {
    @NotNull
    private Integer itemsAmount;

    @NotEmpty
    @Valid
    private ProductDTO product;

    public Integer getItemsAmount() {
        return itemsAmount;
    }

    public void setItemsAmount(Integer itemsAmount) {
        this.itemsAmount = itemsAmount;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
