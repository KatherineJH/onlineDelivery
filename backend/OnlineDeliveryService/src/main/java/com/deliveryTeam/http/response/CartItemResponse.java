package com.deliveryTeam.http.response;

import java.math.BigDecimal;

import com.deliveryTeam.entity.CartItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private String categoryName;

    public static CartItemResponse from(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setCartItemId(cartItem.getCartItemId());
        response.setProductId(cartItem.getProduct().getProductId());
        response.setProductName(cartItem.getProduct().getName());
        response.setPrice(cartItem.getProduct().getPrice());
        response.setQuantity(cartItem.getQuantity());
        response.setCategoryName(cartItem.getProduct().getCategory().getName());
        return response;
    }
}
