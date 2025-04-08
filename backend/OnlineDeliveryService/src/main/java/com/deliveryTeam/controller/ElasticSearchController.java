package com.deliveryTeam.controller;


import com.deliveryTeam.entity.StoreDocument;
import com.deliveryTeam.service.autoSearch.StoreElasticSearchService;
import com.deliveryTeam.service.autoSearch.StoreIndexer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class ElasticSearchController {

    private final StoreElasticSearchService searchService;
    private final StoreIndexer storeIndexer;

    @GetMapping
    public ResponseEntity<List<StoreDocument>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(searchService.search(keyword));
    }

    @PostMapping("/index/stores")
    public ResponseEntity<String> indexStores() {
        storeIndexer.indexAllStores();
        return ResponseEntity.ok("Indexing complete!");
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(@RequestParam String prefix) {
        return ResponseEntity.ok(searchService.autocomplete(prefix));
    }
}
