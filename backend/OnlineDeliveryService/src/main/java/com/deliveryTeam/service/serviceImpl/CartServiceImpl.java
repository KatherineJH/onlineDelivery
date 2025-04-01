package com.deliveryTeam.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliveryTeam.entity.Cart;
import com.deliveryTeam.entity.CartItem;
import com.deliveryTeam.entity.OrderEntity;
import com.deliveryTeam.entity.OrderItem;
import com.deliveryTeam.entity.Product;
import com.deliveryTeam.entity.User;
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
    public Cart getCartByUserId(Long userId) {
        return cartRepository
                .findByUser_UserId(userId)
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

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    // 장바구니에서 아이템 제거
    @Override
    public void removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = getCartByUserId(userId);
        CartItem cartItem =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다."));

        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            throw new RuntimeException("해당 장바구니 아이템에 대한 권한이 없습니다.");
        }

        cartItemRepository.delete(cartItem);
    }

    // 장바구니 아이템 수량 수정
    @Override
    public void updateCartItemQuantity(Long userId, Long cartItemId, int quantity) {
        Cart cart = getCartByUserId(userId);
        CartItem cartItem =
                cartItemRepository
                        .findById(cartItemId)
                        .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다."));

        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            throw new RuntimeException("해당 장바구니 아이템에 대한 권한이 없습니다.");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    // 장바구니 아이템 전체 삭제제
    @Override
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cartItemRepository.deleteAllByCartCartId(cart.getCartId());
    }

    // 장바구니 아이템 기반으로 주문을 생성
    @Override
    public OrderEntity createOrderFromCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        List<CartItem> cartItems = cartItemRepository.findByCartCartId(cart.getCartId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("장바구니가 비어있습니다.");
        }

        // 주문 생성
        OrderEntity order = new OrderEntity();
        order.setUser(cart.getUser());
        order.setStatus("PENDING");
        order = orderRepository.save(order);

        // 주문 아이템 생성
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        // 장바구니 비우기
        clearCart(userId);

        return order;
    }
}
