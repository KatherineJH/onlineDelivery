package com.deliveryTeam.service.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliveryTeam.entity.*;
import com.deliveryTeam.repository.*;
import com.deliveryTeam.service.CartService;
import com.deliveryTeam.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CartService cartService;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public OrderEntity createOrder(String userEmail) {
        User user =
                userRepository
                        .findByEmail(userEmail)
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Cart cart = cartService.getCartByUserId(user.getUserId());
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("장바구니가 비어있습니다.");
        }

        // 모든 상품이 같은 매장의 것인지 확인
        Store store = cart.getCartItems().get(0).getProduct().getStore();
        for (CartItem item : cart.getCartItems()) {
            if (!item.getProduct().getStore().getStoreId().equals(store.getStoreId())) {
                throw new RuntimeException("장바구니에 서로 다른 매장의 상품이 있습니다.");
            }
        }

        // 주문 생성
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStore(store);
        order.setStatus(OrderEntity.OrderStatus.PENDING);

        // 주문 항목 생성
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.addOrderItem(orderItem);
        }

        // 총액 계산
        order.calculateTotalAmount();

        // 장바구니 비우기
        cartService.clearCart(user.getUserId());

        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long orderId, String userEmail) {
        OrderEntity order = getOrderAndValidateUser(orderId, userEmail);

        if (order.getStatus() != OrderEntity.OrderStatus.PENDING) {
            throw new RuntimeException("취소할 수 없는 주문 상태입니다.");
        }

        order.setStatus(OrderEntity.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public OrderEntity updateOrderStatus(Long orderId, String status, String ownerEmail) {
        OrderEntity order = getOrderAndValidateOwner(orderId, ownerEmail);

        try {
            OrderEntity.OrderStatus newStatus =
                    OrderEntity.OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
            return orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("잘못된 주문 상태입니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderEntity getOrder(Long orderId, String email) {
        OrderEntity order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        // 주문한 사용자나 매장 주인만 조회 가능
        if (!order.getUser().getEmail().equals(email)
                && !order.getStore().getOwner().getEmail().equals(email)) {
            throw new RuntimeException("해당 주문에 대한 접근 권한이 없습니다.");
        }

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderEntity> getOrdersByUser(String userEmail) {
        User user =
                userRepository
                        .findByEmail(userEmail)
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderEntity> getOrdersByStore(Long storeId, String ownerEmail) {
        Store store = getStoreAndValidateOwner(storeId, ownerEmail);
        return orderRepository.findByStoreOrderByOrderDateDesc(store);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderEntity> getOrdersByStoreAndStatus(
            Long storeId, String status, String ownerEmail) {
        Store store = getStoreAndValidateOwner(storeId, ownerEmail);
        try {
            OrderEntity.OrderStatus orderStatus =
                    OrderEntity.OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.findByStoreAndStatusOrderByOrderDateDesc(store, orderStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("잘못된 주문 상태입니다.");
        }
    }

    private OrderEntity getOrderAndValidateUser(Long orderId, String userEmail) {
        OrderEntity order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("해당 주문에 대한 접근 권한이 없습니다.");
        }

        return order;
    }

    private OrderEntity getOrderAndValidateOwner(Long orderId, String ownerEmail) {
        OrderEntity order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        if (!order.getStore().getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException("해당 주문에 대한 접근 권한이 없습니다.");
        }

        return order;
    }

    private Store getStoreAndValidateOwner(Long storeId, String ownerEmail) {
        Store store =
                storeRepository
                        .findById(storeId)
                        .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다."));

        if (!store.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException("해당 매장에 대한 접근 권한이 없습니다.");
        }

        return store;
    }
}
