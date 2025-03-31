package com.deliveryTeam.service;

import com.deliveryTeam.entity.Product;

import java.util.List;

public interface ProductService {
    // product id로 검색

    Product getProductById(Long id);

    // 키워드로 검색
    List<Product> findByNameContaining(String name);

    //카테고리로 검색
    List<Product> findByCategoryCategoryId(Long categoryId);

    // 전체 조회
    List<Product> findAll();

    // 등록
    Product save(Product product);
}
