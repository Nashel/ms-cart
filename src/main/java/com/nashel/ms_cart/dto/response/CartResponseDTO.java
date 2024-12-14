package com.nashel.ms_cart.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class CartResponseDTO {
    @NotNull
    private UUID id;

    private LocalDateTime lastUpdate;

    @NotEmpty
    private Float totalPrice;

    @NotEmpty
    @Valid
    private Set<CartItemResponseDTO> cartItems;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<CartItemResponseDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItemResponseDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
