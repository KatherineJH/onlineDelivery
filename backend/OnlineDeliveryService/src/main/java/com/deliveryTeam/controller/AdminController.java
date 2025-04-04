package com.deliveryTeam.controller;

import com.deliveryTeam.dto.ProductDto;
import com.deliveryTeam.dto.OrderStatusDto;
import com.deliveryTeam.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 대시보드 조회
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardData());
    }

    // 상품 등록
    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto productDto) {
        return ResponseEntity.ok(adminService.createProduct(productDto));
    }

    // 상품 수정
    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDto productDto) {
        return ResponseEntity.ok(adminService.updateProduct(id, productDto));
    }

    //  상품 삭제
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // 주문 전체 조회
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(adminService.getAllOrders());
    }

    //  주문 상태 변경
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusDto dto) {
        return ResponseEntity.ok(adminService.updateOrderStatus(id, dto));
    }

    // 사용자 목록 조회
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }
}
