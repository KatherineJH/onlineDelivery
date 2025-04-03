package com.deliveryTeam.service.serviceImpl;

import java.util.List;

import com.deliveryTeam.entity.Category;
import com.deliveryTeam.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import com.deliveryTeam.entity.Product;
import com.deliveryTeam.repository.ProductRepository;
import com.deliveryTeam.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

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

    @Override
    public Product save(Product product) {
        //  카테고리 ID로 영속 객체 가져오기
        if (product.getCategory() != null && product.getCategory().getCategoryId() != null) {
            Long categoryId = product.getCategory().getCategoryId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 카테고리 ID입니다."));
            product.setCategory(category); // 영속화된 객체로 바꾸기
        }

        //  저장
        Product saved = productRepository.save(product);

        // . Lazy 강제 초기화 (category.name)
        if (saved.getCategory() != null) {
            saved.getCategory().getName(); // 이 한 줄이 핵심
        }

        return saved;
    }
}
