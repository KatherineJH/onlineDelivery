package com.deliveryTeam.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.util.List;

@Document(indexName = "stores") // 인덱스 이름도 바꿔주면 좋습니다
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDocument {

    @Id
    private Long id;

    private String name;
    private String cuisineType;
    private List<String> categoryNames;
    private List<String> productNames;

    @CompletionField
    private Completion suggest;
}


