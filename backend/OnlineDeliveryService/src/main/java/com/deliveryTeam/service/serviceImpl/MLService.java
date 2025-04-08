package com.deliveryTeam.service.serviceImpl;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MLService {

    private final RestTemplate restTemplate;

    private final String baseUrl = "http://127.0.0.1:5000";

    public String getPrediction(Map<String, Object> inputData) {
        return postRequest("/predict", inputData); // Flask에서 받은 JSON 그대로 반환
    }

    public String getTopWords() {
        return getRequest("/top-words");
    }

    public String getWorstWords() {
        return getRequest("/worst-words");
    }

    public String getRestaurantRanking(Map<String, Object> inputData) {
        return postRequest("/rank-restaurants", inputData);
    }

    private String postRequest(String endpoint, Map<String, Object> data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);
        return restTemplate.postForObject(baseUrl + endpoint, entity, String.class);
    }

    private String getRequest(String endpoint) {
        return restTemplate.getForObject(baseUrl + endpoint, String.class);
    }
}
