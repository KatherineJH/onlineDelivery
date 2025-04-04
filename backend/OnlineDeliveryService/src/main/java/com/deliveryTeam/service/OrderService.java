package com.deliveryTeam.service;

import java.util.List;

import com.deliveryTeam.entity.OrderEntity;

public interface OrderService {
    OrderEntity createOrder(String userEmail);

    void cancelOrder(Long orderId, String userEmail);

    OrderEntity updateOrderStatus(Long orderId, String status, String ownerEmail);

    OrderEntity getOrder(Long orderId, String email);

    List<OrderEntity> getOrdersByUser(String userEmail);

    List<OrderEntity> getOrdersByStore(Long storeId, String ownerEmail);

    List<OrderEntity> getOrdersByStoreAndStatus(Long storeId, String status, String ownerEmail);
}
