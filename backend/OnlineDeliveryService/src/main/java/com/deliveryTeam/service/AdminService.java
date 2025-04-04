package com.deliveryTeam.service;

import com.deliveryTeam.dto.ProductDto;
import com.deliveryTeam.dto.OrderStatusDto;

public interface AdminService {

    Object getDashboardData();

    Object createProduct(ProductDto dto);

    Object updateProduct(Long id, ProductDto dto);

    void deleteProduct(Long id);

    Object getAllOrders();

    Object updateOrderStatus(Long id, OrderStatusDto dto);

    Object getAllUsers();
}
