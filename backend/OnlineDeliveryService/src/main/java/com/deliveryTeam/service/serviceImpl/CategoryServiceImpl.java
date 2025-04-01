package com.deliveryTeam.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deliveryTeam.entity.Category;
import com.deliveryTeam.repository.CategoryRepository;
import com.deliveryTeam.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("해당 카테고리가 없습니다."));
    }

    @Override
    public List<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
}
