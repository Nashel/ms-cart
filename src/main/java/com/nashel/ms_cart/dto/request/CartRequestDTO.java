package com.nashel.ms_cart.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class CartRequestDTO {
    @Valid
    @NotEmpty
    @Size(min = 1, max = 100)
    private Set<CartItemRequestDTO> cartItems;

    public Set<CartItemRequestDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItemRequestDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
