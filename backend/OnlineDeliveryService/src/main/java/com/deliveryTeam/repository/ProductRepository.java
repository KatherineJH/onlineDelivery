package com.deliveryTeam.repository;

import com.deliveryTeam.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //키워드 검색 ㄷㄷㄷ
   List<Product> findByNameContaining(String name);

   //카테고리로 검색 ㅇㅇㅇ
   List<Product> findByCategory(Long categoryId);

}
