package com.deliveryTeam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliveryTeam.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByName(String name);
}