package com.deliveryTeam.service.serviceImpl;

import java.util.List;

import com.deliveryTeam.dto.ProductDto;
import com.deliveryTeam.entity.Category;
import com.deliveryTeam.entity.Store;
import com.deliveryTeam.repository.CategoryRepository;
import com.deliveryTeam.repository.StoreRepository;
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
    private final StoreRepository storeRepository;

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
    @Override
    public Product createProductFromDto(ProductDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));
        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 매장입니다."));

        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        product.setStore(store);

        return productRepository.save(product);
    }

}
