package com.deliveryTeam.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import com.deliveryTeam.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliveryTeam.repository.CartItemRepository;
import com.deliveryTeam.repository.CartRepository;
import com.deliveryTeam.repository.OrderItemRepository;
import com.deliveryTeam.repository.OrderRepository;
import com.deliveryTeam.repository.ProductRepository;
import com.deliveryTeam.repository.UserRepository;
import com.deliveryTeam.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    // 사용자에게 장바구니 생성 - 회원가입 후 자동실행
    @Override
    public Cart createCart(Long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    // 유저 아이디로 장바구니 조회
    @Override
    @Transactional(readOnly = true)
    public Cart getCartByUserId(Long userId) {
        return cartRepository
                .findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));
    }

    // 장바구니 아이템 추가
    @Override
    public CartItem addItemToCart(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // 장바구니에 같은 상품이 있는지 확인
        CartItem existingCartItem =
                cart.getCartItems().stream()
                        .filter(item -> item.getProduct().getProductId().equals(productId))
                        .findFirst()
                        .orElse(null);

        if (existingCartItem != null) {
            // 기존 상품이 있으면 수량만 증가
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cart.updateTotalPrice();
            return existingCartItem;
        } else {
            // 새로운 상품이면 CartItem 생성
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cart.addCartItem(cartItem);
            return cartItemRepository.save(cartItem);
        }
    }

    // 장바구니에서 아이템 제거
    @Override
    @Transactional
    public void removeItemFromCart(Long userId, Long cartItemId) {
        logger.info("장바구니 상품 삭제 시도 - 사용자ID: {}, 상품ID: {}", userId, cartItemId);

        Cart cart = getCartByUserId(userId);
        logger.debug("사용자의 장바구니 조회 완료 - 장바구니ID: {}", cart.getCartId());

        cart.getCartItems()
                .removeIf(
                        item -> {
                            if (item.getCartItemId().equals(cartItemId)) {
                                logger.debug(
                                        "삭제할 장바구니 상품 찾음 - 상품명: {}", item.getProduct().getName());
                                return true;
                            }
                            return false;
                        });

        cart.updateTotalPrice();
        cartRepository.save(cart);

        logger.info("장바구니 상품 삭제 완료 - 사용자ID: {}, 상품ID: {}", userId, cartItemId);
    }

    // 장바구니 아이템 수량 수정
    @Override
    @Transactional
    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, int quantity) {
        logger.info("장바구니 상품 수량 변경 시도 - 사용자ID: {}, 상품ID: {}, 수량: {}", userId, cartItemId, quantity);

        Cart cart = getCartByUserId(userId);
        logger.debug("사용자의 장바구니 조회 완료 - 장바구니ID: {}", cart.getCartId());

        CartItem cartItem =
                cart.getCartItems().stream()
                        .filter(item -> item.getCartItemId().equals(cartItemId))
                        .findFirst()
                        .orElseThrow(
                                () -> {
                                    logger.error(
                                            "장바구니 상품을 찾을 수 없음 - 사용자ID: {}, 상품ID: {}",
                                            userId,
                                            cartItemId);
                                    return new RuntimeException("Cart item not found");
                                });

        logger.debug(
                "장바구니 상품 찾음 - 상품명: {}, 현재수량: {}",
                cartItem.getProduct().getName(),
                cartItem.getQuantity());

        cartItem.setQuantity(quantity);
        cart.updateTotalPrice();
        cartRepository.save(cart);

        logger.info("장바구니 상품 수량 변경 완료 - 사용자ID: {}, 상품ID: {}, 수량: {}", userId, cartItemId, quantity);
        return cartItem;
    }

    // 장바구니 아이템 전체 삭제제
    @Override
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.clearItems();
        cartRepository.save(cart);
    }

    // 장바구니 아이템 기반으로 주문을 생성
    @Override
    public OrderEntity createOrderFromCart(Long userId) {
        //  유저 장바구니 가져오기
        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("장바구니가 존재하지 않습니다."));

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new RuntimeException("장바구니가 비어 있습니다.");
        }

        // 주문 객체 생성
        OrderEntity order = new OrderEntity();
        order.setUser(cart.getUser());

        // store는 첫 상품 기준
        Store store = cartItems.get(0).getProduct().getStore();
        order.setStore(store);

        // 주문 항목 구성
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            order.addOrderItem(orderItem);
        }

        // 총액 계산
        order.calculateTotalAmount();

        // 주문 저장
        orderRepository.save(order);

        // 장바구니 비우기
        cart.getCartItems().clear(); // 관계 끊기
        cartRepository.save(cart);  // 업데이트 반영

        return order;
    }

}
