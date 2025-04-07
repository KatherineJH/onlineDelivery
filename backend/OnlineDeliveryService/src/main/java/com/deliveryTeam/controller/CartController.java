package com.deliveryTeam.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.deliveryTeam.entity.Cart;
import com.deliveryTeam.entity.CartItem;
import com.deliveryTeam.entity.OrderEntity;
import com.deliveryTeam.entity.User;
import com.deliveryTeam.http.request.AddCartItemRequest;
import com.deliveryTeam.http.response.CartItemResponse;
import com.deliveryTeam.http.response.CartResponse;
import com.deliveryTeam.service.CartService;
import com.deliveryTeam.service.UserService;

import lombok.RequiredArgsConstructor;

/** 장바구니 관련 기능을 처리하는 컨트롤러 장바구니 조회, 상품 추가/수정/삭제, 주문 전환 등의 기능을 제공 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    /**
     * 현재 사용자의 장바구니 정보를 조회 장바구니에 담긴 상품 목록, 총 금액 등의 정보를 반환
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        Cart cart = cartService.getCartByUserId(user.getUserId());

        return ResponseEntity.ok(CartResponse.from(cart));
    }

    /**
     * 장바구니에 상품을 추가
     *
     * @param request
     * @return
     */
    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addItemToCart(
            @RequestBody AddCartItemRequest request, Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        CartItem cartItem =
                cartService.addItemToCart(
                        user.getUserId(), request.getProductId(), request.getQuantity());

        return ResponseEntity.ok(CartItemResponse.from(cartItem));
    }

    /**
     * 장바구니에서 특정 상품을 삭제
     *
     * @param cartItemId
     * @return
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable Long cartItemId, Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        cartService.removeItemFromCart(user.getUserId(), cartItemId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 장바구니 상품의 수량을 수정
     *
     * @param cartItemId
     * @param request
     * @return 수정된 장바구니 상품 정보
     */
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody Map<String, Integer> request,
            Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        CartItem updatedItem =
                cartService.updateCartItemQuantity(
                        user.getUserId(), cartItemId, request.get("quantity"));

        return ResponseEntity.ok(CartItemResponse.from(updatedItem));
    }

    /**
     * 장바구니의 상품들로 주문을 생성 주문 생성 후 장바구니는 비워짐
     *
     * @return 생성된 주문 정보
     */
    @PostMapping("/checkout")
    public ResponseEntity<OrderEntity> checkout(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        OrderEntity order = cartService.createOrderFromCart(user.getUserId());

        return ResponseEntity.ok(order);
    }

    /**
     * 장바구니를 비움 장바구니에 담긴 모든 상품을 제거
     *
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        cartService.clearCart(user.getUserId());

        return ResponseEntity.noContent().build();
    }
}
