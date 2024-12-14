package com.nashel.ms_cart.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "update_TS", nullable = false)
    private LocalDateTime updateTS;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "total_price", nullable = false)
    private float totalPrice;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private Set<CartItem> cartItems = new HashSet<>();

    @PrePersist
    @PreUpdate
    public void updateLastAccessTime() {
        this.updateTS = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getUpdateTS() {
        return updateTS;
    }

    public void setUpdateTS(LocalDateTime updateTS) {
        this.updateTS = updateTS;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
