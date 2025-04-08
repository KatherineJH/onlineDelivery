package com.deliveryTeam.repository;

import com.deliveryTeam.entity.StoreDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StoreElasticSearchRepository extends ElasticsearchRepository<StoreDocument, Long> {
}

