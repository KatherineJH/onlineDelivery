package com.deliveryTeam.service.serviceImpl;

import com.deliveryTeam.dto.OrderStatusDto;
import com.deliveryTeam.dto.ProductDto;
import com.deliveryTeam.entity.*;
import com.deliveryTeam.repository.*;
import com.deliveryTeam.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;  // store 아직 미구현

    @Override
    public Object getDashboardData() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalUsers", userRepository.count());
        result.put("totalProducts", productRepository.count());
        result.put("totalOrders", orderRepository.count());
        return result;
    }

//    store 현재 미구현
    @Override
    public Object createProduct(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
            product.setCategory(category);
        }

        if (dto.getStoreId() != null) {
            Store store = storeRepository.findById(dto.getStoreId())    // store 아직 미구현
                    .orElseThrow(() -> new RuntimeException("스토어 없음"));
            product.setStore(store);
        }

        return productRepository.save(product);
    }

    @Override
    public Object updateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
            product.setCategory(category);
        }

        if (dto.getStoreId() != null) {
            Store store = storeRepository.findById(dto.getStoreId())
                    .orElseThrow(() -> new RuntimeException("스토어 없음"));
            product.setStore(store);
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Object getAllOrders() {
        return orderRepository.findAll(); // or DTO 변환
    }

    @Override
    public Object updateOrderStatus(Long id, OrderStatusDto dto) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        order.setStatus(dto.getStatus());
        return orderRepository.save(order);
    }

    @Override
    public Object getAllUsers() {
        return userRepository.findAll(); // or DTO 변환
    }
}
