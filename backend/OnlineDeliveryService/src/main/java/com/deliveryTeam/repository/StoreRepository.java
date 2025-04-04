package com.deliveryTeam.repository;

import com.deliveryTeam.entity.Store;
import com.deliveryTeam.entity.CUISINE_TYPE;
import com.deliveryTeam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByOwner(User owner);

    List<Store> findByCuisineType(CUISINE_TYPE cuisineType);

    List<Store> findByNameContaining(String name);
}