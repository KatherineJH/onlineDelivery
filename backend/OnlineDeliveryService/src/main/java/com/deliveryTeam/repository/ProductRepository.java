package com.deliveryTeam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliveryTeam.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 키워드 검색 ㄷㄷㄷ
    List<Product> findByNameContaining(String name);

    // 카테고리로 검색 ㅇㅇㅇ
    List<Product> findByCategoryCategoryId(Long categoryId);
}
