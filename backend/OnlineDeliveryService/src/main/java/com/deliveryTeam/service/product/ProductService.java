package com.deliveryTeam.service.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deliveryTeam.entity.Product;
import com.deliveryTeam.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // product id로 검색
    public Product getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품이 존재하지 않습니다."));
    }

    // 키워드로 검색
    public List<Product> findByNameContaining(String name) {
        return productRepository.findByNameContaining(name);
    }

    // 카테고리로 검색
    public List<Product> findByCategoryCategoryId(Long categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId);
    }

    // 전체 조회
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // 등록
    public Product save(Product product) {
        return productRepository.save(product);
    }
}
