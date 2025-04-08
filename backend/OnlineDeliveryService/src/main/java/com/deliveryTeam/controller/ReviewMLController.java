package com.deliveryTeam.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deliveryTeam.entity.Review;
import com.deliveryTeam.entity.Store;
import com.deliveryTeam.entity.User;
import com.deliveryTeam.repository.ReviewRepository;
import com.deliveryTeam.repository.StoreRepository;
import com.deliveryTeam.service.auth.CustomUserService;
import com.deliveryTeam.service.serviceImpl.MLService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/predict")
@RequiredArgsConstructor
public class ReviewMLController {

    private final MLService mlService;
    private final CustomUserService customUserService;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> requestPrediction(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> inputData) {

        try {
            User user = customUserService.findUserByJwtToken(token);
            Long restaurantId = Long.valueOf(inputData.get("restaurantId").toString());
            Store store =
                    storeRepository
                            .findById(restaurantId)
                            .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            String response = mlService.getPrediction(inputData); // 이건 JSON string임

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response); // 안전하게 JSON 파싱

            String predictedLabel = responseJson.get("prediction").asText();
            double confidence = responseJson.get("probability").asDouble();

            Review review = new Review();
            review.setContent(inputData.get("review").toString());
            review.setRating(predictedLabel);
            review.setPercentage(confidence * 100);
            review.setStore(store);
            review.setUser(user);

            reviewRepository.save(review);

            Map<String, Object> result = new HashMap<>();
            result.put("prediction", predictedLabel);
            result.put("confidence", confidence * 100);
            result.put("message", "Review saved successfully");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Prediction failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/top-words")
    public String getTopWords() {
        return mlService.getTopWords();
    }

    @GetMapping("/worst-words")
    public String getWorstWords() {
        return mlService.getWorstWords();
    }

    @PostMapping("/rank-restaurants")
    public String rankRestaurants(@RequestBody Map<String, Object> inputData) {
        return mlService.getRestaurantRanking(inputData);
    }
}
