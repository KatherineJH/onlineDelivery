package com.deliveryTeam.service;

import com.deliveryTeam.entity.Cart;
import com.deliveryTeam.entity.CartItem;
import com.deliveryTeam.entity.OrderEntity;

public interface CartService {
    Cart createCart(Long userId);

    Cart getCartByUserId(Long userId);

    CartItem addItemToCart(Long userId, Long productId, int quantity);

    void removeItemFromCart(Long userId, Long cartItemId);

    void updateCartItemQuantity(Long userId, Long cartItemId, int quantity);

    void clearCart(Long userId);

    OrderEntity createOrderFromCart(Long userId);
}
