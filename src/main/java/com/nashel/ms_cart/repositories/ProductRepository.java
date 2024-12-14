package com.nashel.ms_cart.repositories;

import com.nashel.ms_cart.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
