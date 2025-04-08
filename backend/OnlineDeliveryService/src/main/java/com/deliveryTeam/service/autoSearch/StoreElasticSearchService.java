package com.deliveryTeam.service.autoSearch;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deliveryTeam.entity.StoreDocument;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreElasticSearchService {

    private final ElasticsearchClient client;

    public List<StoreDocument> search(String keyword) {
        try {
            SearchResponse<StoreDocument> response =
                    client.search(
                            s ->
                                    s.index("stores")
                                            .query(
                                                    q ->
                                                            q.multiMatch(
                                                                    m ->
                                                                            m.query(keyword)
                                                                                    .fields(
                                                                                            "name",
                                                                                            "cuisineType",
                                                                                            "productNames",
                                                                                            "categoryNames")
                                                                                    .fuzziness(
                                                                                            "AUTO"))),
                            StoreDocument.class);

            return response.hits().hits().stream().map(Hit::source).toList();

        } catch (IOException e) {
            throw new RuntimeException("검색 중 오류", e);
        }
    }

    //    public List<String> autocomplete(String prefix) {
    //        try {
    //            var response = client.search(s -> s
    //                            .index("stores")
    //                            .suggest(sg -> sg
    //                                    .suggesters("store-suggest", sugg -> sugg
    //                                            .prefix(prefix.toLowerCase())
    //                                            .completion(c -> c
    //                                                    .field("suggest")
    //                                                    .skipDuplicates(true)
    //                                                    .size(10)
    //                                            )
    //                                    )
    //                            ),
    //                    StoreDocument.class
    //            );
    //
    //            return response.suggest()
    //                    .get("store-suggest").stream()
    //                    .flatMap(suggestion -> suggestion.completion().options().stream())
    //                    .map(option -> option.text())
    //                    .distinct()
    //                    .toList();
    //
    //        } catch (IOException e) {
    //            throw new RuntimeException("자동완성 실패", e);
    //        }
    //    }
    public List<String> autocomplete(String prefix) {
        try {
            SearchResponse<StoreDocument> response =
                    client.search(
                            s ->
                                    s.index("stores")
                                            .suggest(
                                                    sug ->
                                                            sug.suggesters(
                                                                    "store-suggest",
                                                                    cs ->
                                                                            cs.prefix(prefix)
                                                                                    .completion(
                                                                                            c ->
                                                                                                    c.field(
                                                                                                                    "suggest")
                                                                                                            .skipDuplicates(
                                                                                                                    true)
                                                                                                            .size(
                                                                                                                    10)))),
                            StoreDocument.class);

            // 자동완성 결과 파싱
            return response.suggest().get("store-suggest").stream()
                    .flatMap(suggestion -> suggestion.completion().options().stream())
                    .map(option -> option.text())
                    .distinct()
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("자동완성 검색 실패", e);
        }
    }
}
