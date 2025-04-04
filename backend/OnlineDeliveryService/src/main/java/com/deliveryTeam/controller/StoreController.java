package com.deliveryTeam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.deliveryTeam.dto.StoreDTO;
import com.deliveryTeam.dto.StoreResponseDTO;
import com.deliveryTeam.entity.CUISINE_TYPE;
import com.deliveryTeam.entity.Store;
import com.deliveryTeam.service.StoreService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 매장 조회 관련 API를 처리하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 매장 상세 정보 조회
     */
    @GetMapping("/api/stores/{storeId}")
    public ResponseEntity<StoreResponseDTO> getStore(@PathVariable Long storeId) {
        Store store = storeService.getStoreById(storeId);
        return ResponseEntity.ok(StoreResponseDTO.from(store));
    }

    /**
     * 전체 매장 목록 조회
     */
    @GetMapping("/api/stores")
    public ResponseEntity<List<StoreResponseDTO>> getAllStores() {
        List<Store> stores = storeService.getAllStores();
        List<StoreResponseDTO> response = stores.stream()
                .map(StoreResponseDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * 음식 종류별 매장 조회
     */
    @GetMapping("/api/stores/cuisine/{cuisineType}")
    public ResponseEntity<List<StoreResponseDTO>> getStoresByCuisineType(
            @PathVariable CUISINE_TYPE cuisineType) {
        List<Store> stores = storeService.getStoresByCuisineType(cuisineType);
        List<StoreResponseDTO> response = stores.stream()
                .map(StoreResponseDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * 매장 이름으로 검색
     */
    @GetMapping("/api/stores/search")
    public ResponseEntity<List<StoreResponseDTO>> searchStoresByName(@RequestParam String name) {
        List<Store> stores = storeService.searchStoresByName(name);
        List<StoreResponseDTO> response = stores.stream()
                .map(StoreResponseDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}

/**
 * 매장 관리 관련 점주 전용 API를 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
class StoreManagementController {

    private final StoreService storeService;

    /**
     * 매장 등록 (점주 전용)
     */
    @PostMapping("/stores")
    public ResponseEntity<StoreResponseDTO> createStore(
            @RequestBody StoreDTO storeDTO,
            @AuthenticationPrincipal String ownerEmail) {
        Store store = storeService.createStore(storeDTO, ownerEmail);
        return ResponseEntity.ok(StoreResponseDTO.from(store));
    }

    /**
     * 매장 정보 수정 (점주 전용)
     */
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDTO> updateStore(
            @PathVariable Long storeId,
            @RequestBody StoreDTO storeDTO,
            @AuthenticationPrincipal String ownerEmail) {
        Store store = storeService.updateStore(storeId, storeDTO, ownerEmail);
        return ResponseEntity.ok(StoreResponseDTO.from(store));
    }

    /**
     * 매장 삭제 (점주 전용)
     */
    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable(name = "storeId") Long storeId,
            @AuthenticationPrincipal String ownerEmail) {
        storeService.deleteStore(storeId, ownerEmail);
        return ResponseEntity.noContent().build();
    }

    /**
     * 내 매장 목록 조회 (점주 전용)
     */
    @GetMapping("/stores/my-stores")
    public ResponseEntity<List<StoreResponseDTO>> getMyStores(@AuthenticationPrincipal String ownerEmail) {
        List<Store> stores = storeService.getStoresByOwner(ownerEmail);
        List<StoreResponseDTO> response = stores.stream()
                .map(StoreResponseDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}