package com.nashel.ms_cart.controllers;

import com.nashel.ms_cart.dto.request.CartRequestDTO;
import com.nashel.ms_cart.dto.response.CartResponseDTO;
import com.nashel.ms_cart.services.interfaces.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rest/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable("id") UUID cartId) {
        CartResponseDTO createdCart = cartService.getCartById(cartId);
        return new ResponseEntity<>(createdCart, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart(@Valid @RequestBody CartRequestDTO cartDTO) {
        CartResponseDTO createdCart = cartService.createCart(cartDTO);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartResponseDTO> updateCart(@PathVariable("id") UUID cartId, @Valid @RequestBody CartRequestDTO cartDTO) {
        CartResponseDTO updatedCart = cartService.updateCartItems(cartId, cartDTO);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CartResponseDTO> deleteCart(@PathVariable("id") UUID cartId) {
        CartResponseDTO deletedCart = cartService.deleteCartById(cartId);
        return new ResponseEntity<>(deletedCart, HttpStatus.OK);
    }
}
