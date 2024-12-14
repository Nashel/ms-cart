package com.nashel.ms_cart.services.impl;

import com.nashel.ms_cart.dto.request.CartItemRequestDTO;
import com.nashel.ms_cart.dto.request.CartRequestDTO;
import com.nashel.ms_cart.dto.response.CartResponseDTO;
import com.nashel.ms_cart.mappers.CartMapper;
import com.nashel.ms_cart.models.Cart;
import com.nashel.ms_cart.models.CartItem;
import com.nashel.ms_cart.models.Product;
import com.nashel.ms_cart.repositories.CartItemRepository;
import com.nashel.ms_cart.repositories.CartRepository;
import com.nashel.ms_cart.repositories.ProductRepository;
import com.nashel.ms_cart.services.interfaces.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;


    @Override
    public CartResponseDTO createCart(CartRequestDTO cartRequestDTO) {
        Cart newCart = new Cart();

        Set<CartItemRequestDTO> requestItems = cartRequestDTO.getCartItems();

        Set<CartItem> newCartItems = new HashSet<>();

        // Validations

        Set<Long> itemIdCheckSet = new HashSet<>();
        for (CartItemRequestDTO cartItem : requestItems) {
            if (!itemIdCheckSet.add(cartItem.getProductId())) {
                throw new IllegalArgumentException("Duplicate ID found: " + cartItem.getProductId().toString());
            }
        }

        // Mapping items inside the Cart
        for (CartItemRequestDTO requestItem : requestItems) {
            Product product = productRepository.findById(requestItem.getProductId()).orElse(null);
            if (product != null) {

                // Add item to cart list
                CartItem newCartItem = new CartItem();

                newCartItem.setProduct(product);
                newCartItem.setCart(newCart);
                newCartItem.setItemsAmount(requestItem.getItemsAmount());

                newCartItems.add(newCartItem);
            }
        }

        float cartTotalPrice = calculateCartPrice(newCartItems);

        // Mapping Cart
        newCart.setCreatedAt(LocalDateTime.now());
        newCart.setUpdateTS(LocalDateTime.now());
        newCart.setTotalPrice(cartTotalPrice);

        newCart.setCartItems(newCartItems);

        // Store data
        Cart createdCart = cartRepository.save(newCart);


        return CartMapper.toResponseDTO(createdCart);
    }

    public CartResponseDTO getCartById (UUID cartId) {
        Cart cartEntity = cartRepository.findById(cartId).orElseThrow(() -> new NoSuchElementException("Cart not found with ID: " + cartId));
        return CartMapper.toResponseDTO(cartEntity);
    }

    public CartResponseDTO updateCartItems (UUID cartId, CartRequestDTO cartRequestDTO) {
        Cart cart2Update = cartRepository.findById(cartId).orElseThrow(() -> new NoSuchElementException("Cart not found with ID: " + cartId));

        Set<CartItemRequestDTO> requestItems = cartRequestDTO.getCartItems();

        Set<CartItem> cartItems2Update = cart2Update.getCartItems();

        // Validations

        Set<Long> itemIdCheckSet = new HashSet<>();
        for (CartItemRequestDTO cartItem : requestItems) {
            if (!itemIdCheckSet.add(cartItem.getProductId())) {
                throw new IllegalArgumentException("Duplicate ID found: " + cartItem.getProductId().toString());
            }
        }

        // Mapping items inside the Cart
        for (CartItemRequestDTO requestItem : requestItems) {
            Product product = productRepository.findById(requestItem.getProductId()).orElse(null);
            if (product != null) {

                // Check if product is already in list
                boolean cartContainsItem = false;
                Long itemIdFound = 0L;
                for (CartItem item2Update : cartItems2Update) {
                    if (item2Update.getProduct().getId().equals(product.getId())) {
                        cartContainsItem = true;
                        itemIdFound = item2Update.getId();
                        break;
                    }
                }

                CartItem cartItem2Update;

                // Update depending on existence in Set
                if (cartContainsItem) {
                    cartItem2Update = cartItemRepository.findById(itemIdFound).orElse(null);

                    if (cartItem2Update != null) {
                        cartItem2Update.setItemsAmount(cartItem2Update.getItemsAmount() + requestItem.getItemsAmount());
                    }
                } else {
                    cartItem2Update = new CartItem();

                    cartItem2Update.setProduct(product);
                    cartItem2Update.setCart(cart2Update);
                    cartItem2Update.setItemsAmount(requestItem.getItemsAmount());

                    cartItems2Update.add(cartItem2Update);
                }
            }
        }

        float cartTotalPrice = calculateCartPrice(cartItems2Update);

        // Mapping Cart
        cart2Update.setCreatedAt(LocalDateTime.now());
        cart2Update.setUpdateTS(LocalDateTime.now());
        cart2Update.setTotalPrice(cartTotalPrice);

        cart2Update.setCartItems(cartItems2Update);

        // Store data
        Cart createdCart = cartRepository.save(cart2Update);

        return CartMapper.toResponseDTO(createdCart);
    }

    public CartResponseDTO deleteCartById (UUID cartId) {
        Cart cartEntity = cartRepository.findById(cartId).orElseThrow(() -> new NoSuchElementException("Cart not found with ID: " + cartId));
        CartResponseDTO cartResponse = CartMapper.toResponseDTO(cartEntity);

        cartRepository.delete(cartEntity);

        return cartResponse;
    }

    float calculateCartPrice (Set<CartItem> itemSet) {
        float cartTotalPrice = 0.0f;
        for (CartItem item : itemSet) {
            cartTotalPrice += item.getItemsAmount() * item.getProduct().getAmount();
        }

        return cartTotalPrice;
    }

    @Scheduled(fixedRateString = "${cart.cleanup.interval}")
    void deleteInactiveCarts() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        List<Cart> inactiveCarts = cartRepository.findByUpdateTSBefore(cutoffTime);
        log.debug("Checking for inactive carts");

        for (Cart cart : inactiveCarts) {
            log.info("Deleting inactive cart with ID: {}", cart.getId());
            cartRepository.delete(cart);
        }
    }
}
