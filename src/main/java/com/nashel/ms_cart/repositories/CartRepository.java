package com.nashel.ms_cart.repositories;

import com.nashel.ms_cart.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    List<Cart> findByUpdateTSBefore(LocalDateTime cutoffTime);

}
