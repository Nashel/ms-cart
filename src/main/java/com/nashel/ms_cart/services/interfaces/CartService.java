package com.nashel.ms_cart.services.interfaces;

import com.nashel.ms_cart.dto.request.CartRequestDTO;
import com.nashel.ms_cart.dto.response.CartResponseDTO;

import java.util.UUID;

public interface CartService {

    CartResponseDTO createCart(CartRequestDTO cartRequestDTO);

    CartResponseDTO getCartById (UUID cartId);

    CartResponseDTO updateCartItems (UUID cartId, CartRequestDTO cartRequestDTO);

    CartResponseDTO deleteCartById (UUID cartId);
}
