package com.deliveryTeam.http.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.deliveryTeam.entity.Cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponse {
    private Long cartId;
    private BigDecimal totalPrice;
    private List<CartItemResponse> items;

    public static CartResponse from(Cart cart) {
        CartResponse response = new CartResponse();
        response.setCartId(cart.getCartId());
        response.setTotalPrice(cart.getTotalPrice());
        response.setItems(
                cart.getCartItems().stream()
                        .map(CartItemResponse::from)
                        .collect(Collectors.toList()));
        return response;
    }
}
