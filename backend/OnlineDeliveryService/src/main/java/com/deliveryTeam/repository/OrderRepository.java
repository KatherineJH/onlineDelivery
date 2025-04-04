package com.deliveryTeam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliveryTeam.entity.OrderEntity;
import com.deliveryTeam.entity.Store;
import com.deliveryTeam.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserOrderByOrderDateDesc(User user);

    List<OrderEntity> findByStoreOrderByOrderDateDesc(Store store);

    List<OrderEntity> findByStoreAndStatusOrderByOrderDateDesc(
            Store store, OrderEntity.OrderStatus status);
}
