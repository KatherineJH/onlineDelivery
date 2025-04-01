package com.deliveryTeam.service;

import java.util.List;

import com.deliveryTeam.entity.Category;

public interface CategoryService {

    Category getCategoryById(Long id);

    List<Category> findAll();

    List<Category> findByName(String name);

    Category save(Category category);
}