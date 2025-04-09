package com.deliveryTeam.service.autoSearch;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.stereotype.Service;

import com.deliveryTeam.entity.Product;
import com.deliveryTeam.entity.Store;
import com.deliveryTeam.entity.StoreDocument;
import com.deliveryTeam.repository.StoreRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreIndexer {

    private final StoreRepository storeRepository;
    private final ElasticsearchClient elasticsearchClient;

    public void indexAllStores() {
        List<Store> stores = storeRepository.findAll();

        for (Store store : stores) {
            List<Product> products = store.getProducts();

            List<String> productNames = products.stream().map(Product::getName).toList();

            List<String> foodCategories =
                    products.stream().map(p -> p.getCategory().getName()).distinct().toList();

            Completion suggest = new Completion();

            List<String> suggestInputs =
                    Stream.of(
                                    // 전체 이름
                                    Stream.of(store.getName().toLowerCase()),
                                    Stream.of(store.getCuisineType().name().toLowerCase()),

                                    // 제품 이름 전체
                                    productNames.stream().map(String::toLowerCase),

                                    // 제품 이름 단어 쪼개기
                                    productNames.stream()
                                            .flatMap(
                                                    name ->
                                                            Stream.of(
                                                                    name.toLowerCase().split(" "))))
                            .filter(Objects::nonNull)
                            .flatMap(s -> s)
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .distinct()
                            .toList();

            suggest.setInput(suggestInputs.toArray(new String[0]));

            StoreDocument doc =
                    StoreDocument.builder()
                            .id(store.getStoreId())
                            .name(store.getName())
                            .cuisineType(store.getCuisineType().name())
                            .productNames(productNames)
                            .categoryNames(foodCategories)
                            .suggest(suggest)
                            .build();

            try {
                elasticsearchClient.index(
                        i -> i.index("stores").id(String.valueOf(doc.getId())).document(doc));
            } catch (IOException e) {
                throw new RuntimeException("엘라스틱서치 인덱싱 실패", e);
            }
        }
    }
}
