package com.deliveryTeam.service;

import java.util.List;

import com.deliveryTeam.entity.CUISINE_TYPE;
import com.deliveryTeam.entity.Store;
import com.deliveryTeam.http.request.StoreDTO;

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
