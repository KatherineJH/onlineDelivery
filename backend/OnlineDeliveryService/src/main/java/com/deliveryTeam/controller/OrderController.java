package com.deliveryTeam.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.deliveryTeam.entity.OrderEntity;
import com.deliveryTeam.service.OrderService;

import lombok.RequiredArgsConstructor;

/** 일반 사용자의 주문 관련 API를 처리하는 컨트롤러 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /** 주문 생성 */
    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(Authentication authentication) {
        return ResponseEntity.ok(orderService.createOrder(authentication.getName()));
    }

    /** 주문 취소 */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId, Authentication authentication) {
        orderService.cancelOrder(orderId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    /** 단일 주문 조회 */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrder(
            @PathVariable Long orderId, Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrder(orderId, authentication.getName()));
    }

    /** 사용자의 주문 목록 조회 */
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderEntity>> getMyOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrdersByUser(authentication.getName()));
    }
}

/** 점주의 주문 관리 관련 API를 처리하는 컨트롤러 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
class OrderManagementController {

    private final OrderService orderService;

    /** 주문 상태 업데이트 */
//    @PutMapping("/orders/{orderId}/status")
//    public ResponseEntity<OrderEntity> updateOrderStatus(
//            @PathVariable Long orderId,
//            @RequestParam String status,
//            Authentication authentication) {
//        return ResponseEntity.ok(
//                orderService.updateOrderStatus(orderId, status, authentication.getName()));
//    }

    /** 매장의 주문 목록 조회 */
    @GetMapping("/orders/store/{storeId}")
    public ResponseEntity<List<OrderEntity>> getStoreOrders(
            @PathVariable Long storeId,
            @RequestParam(required = false) String status,
            Authentication authentication) {
        if (status != null) {
            return ResponseEntity.ok(
                    orderService.getOrdersByStoreAndStatus(
                            storeId, status, authentication.getName()));
        }
        return ResponseEntity.ok(orderService.getOrdersByStore(storeId, authentication.getName()));
    }
}
