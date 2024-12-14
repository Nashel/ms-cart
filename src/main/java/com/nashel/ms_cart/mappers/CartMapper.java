package com.nashel.ms_cart.mappers;

import com.nashel.ms_cart.dto.common.ProductDTO;
import com.nashel.ms_cart.dto.response.CartItemResponseDTO;
import com.nashel.ms_cart.dto.response.CartResponseDTO;
import com.nashel.ms_cart.models.Cart;
import com.nashel.ms_cart.models.Product;

import java.util.HashSet;
import java.util.Set;

public class CartMapper {

    public static CartResponseDTO toResponseDTO(Cart cart) {
        if (cart == null) {
            return null;
        }
        CartResponseDTO cartDTO = new CartResponseDTO();

        cartDTO.setId(cart.getId());
        cartDTO.setLastUpdate(cart.getUpdateTS());
        cartDTO.setTotalPrice(cart.getTotalPrice());

        // Prepare items in response
        Set<CartItemResponseDTO> cartItemResponseSet = new HashSet<>();
        cart.getCartItems().forEach(
                item -> {
                    ProductDTO productResponse = new ProductDTO();

                    Product createdProductEntity = item.getProduct();

                    productResponse.setId(createdProductEntity.getId());
                    productResponse.setDescription(createdProductEntity.getDescription());
                    productResponse.setAmount(createdProductEntity.getAmount());

                    CartItemResponseDTO itemResponse = new CartItemResponseDTO();
                    itemResponse.setProduct(productResponse);
                    itemResponse.setItemsAmount(item.getItemsAmount());
                    cartItemResponseSet.add(itemResponse);
                }
        );

        cartDTO.setCartItems(cartItemResponseSet);

        return cartDTO;
    }

}
