package com.deliveryTeam.controller;

import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            Cart cart = cartService.getCartByUserId(user.getUserId());
            CartResponse response = new CartResponse();
            response.setCartId(cart.getCartId());
            response.setTotalPrice(cart.getTotalPrice());
            response.setItems(cart.getCartItems().stream()
                    .map(this::convertToCartItemResponse)
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 장바구니에 상품추가
    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addItemToCart(@RequestBody AddCartItemRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            CartItem cartItem = cartService.addItemToCart(user.getUserId(), request.getProductId(),
                    request.getQuantity());
            return ResponseEntity.ok(convertToCartItemResponse(cartItem));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 장바구니 아이템 삭제
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable(name = "cartItemId") Long cartItemId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            cartService.removeItemFromCart(user.getUserId(), cartItemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 장바구니 아이템 수량 수정
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @PathVariable(name = "cartItemId") Long cartItemId,
            @RequestBody Map<String, Integer> request,
            @AuthenticationPrincipal String email) {
        try {
            logger.info("장바구니 상품 수량 변경 시도 - 사용자: {}, 상품ID: {}, 수량: {}", email, cartItemId, request.get("quantity"));
            User user = userService.getUserByEmail(email);
            CartItem updatedItem = cartService.updateCartItemQuantity(user.getUserId(), cartItemId,
                    request.get("quantity"));
            logger.info("장바구니 상품 수량 변경 성공 - 사용자: {}, 상품ID: {}", email, cartItemId);
            return ResponseEntity.ok(updatedItem);
        } catch (Exception e) {
            logger.error("장바구니 상품 수량 변경 실패 - 사용자: {}, 상품ID: {}, 오류: {}", email, cartItemId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    private CartItemResponse convertToCartItemResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setCartItemId(cartItem.getCartItemId());
        response.setProductId(cartItem.getProduct().getProductId());
        response.setProductName(cartItem.getProduct().getName());
        response.setPrice(cartItem.getProduct().getPrice());
        response.setQuantity(cartItem.getQuantity());
        response.setCategoryName(cartItem.getProduct().getCategory().getName());
        return response;
    }

    // 주문 생성
    @PostMapping("/checkout")
    public ResponseEntity<OrderEntity> checkout() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            OrderEntity order = cartService.createOrderFromCart(user.getUserId());
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 장바구니 비우기
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            cartService.clearCart(user.getUserId());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
