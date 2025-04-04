package com.deliveryTeam.service;

import com.deliveryTeam.entity.Store;
import com.deliveryTeam.entity.CUISINE_TYPE;
import com.deliveryTeam.dto.StoreDTO;

import java.util.List;

public interface StoreService {
    Store createStore(StoreDTO storeDTO, String ownerEmail);

    Store updateStore(Long storeId, StoreDTO storeDTO, String ownerEmail);

    void deleteStore(Long storeId, String ownerEmail);

    Store getStoreById(Long storeId);

    List<Store> getAllStores();

    List<Store> getStoresByOwner(String ownerEmail);

    List<Store> getStoresByCuisineType(CUISINE_TYPE cuisineType);

    List<Store> searchStoresByName(String name);
}