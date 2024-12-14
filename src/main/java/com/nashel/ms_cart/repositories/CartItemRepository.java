package com.nashel.ms_cart.repositories;

import com.nashel.ms_cart.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
