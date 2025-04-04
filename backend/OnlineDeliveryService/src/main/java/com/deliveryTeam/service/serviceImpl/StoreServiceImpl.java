package com.deliveryTeam.service.serviceImpl;

import com.deliveryTeam.entity.Store;
import com.deliveryTeam.entity.User;
import com.deliveryTeam.entity.Address;
import com.deliveryTeam.entity.CUISINE_TYPE;
import com.deliveryTeam.dto.StoreDTO;
import com.deliveryTeam.repository.StoreRepository;
import com.deliveryTeam.repository.UserRepository;
import com.deliveryTeam.service.StoreService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Override
    public Store createStore(StoreDTO storeDTO, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Store store = new Store();
        store.setName(storeDTO.getName());
        store.setLocation(storeDTO.getLocation());
        store.setCuisineType(storeDTO.getCuisineType());
        store.setOwner(owner);

        // 주소 설정
        Address address = new Address();
        address.setStreetName(storeDTO.getStreetName());
        address.setCity(storeDTO.getCity());
        address.setState(storeDTO.getState());
        address.setZipCode(storeDTO.getZipCode());
        address.setStore(store);
        address.setAddressType(Address.AddressType.STORE);
        store.setAddress(address);

        return storeRepository.save(store);
    }

    @Override
    public Store updateStore(Long storeId, StoreDTO storeDTO, String ownerEmail) {
        Store store = getStoreAndValidateOwner(storeId, ownerEmail);

        store.setName(storeDTO.getName());
        store.setLocation(storeDTO.getLocation());
        store.setCuisineType(storeDTO.getCuisineType());

        // 주소 업데이트
        Address address = store.getAddress();
        address.setStreetName(storeDTO.getStreetName());
        address.setCity(storeDTO.getCity());
        address.setState(storeDTO.getState());
        address.setZipCode(storeDTO.getZipCode());

        return storeRepository.save(store);
    }

    @Override
    public void deleteStore(Long storeId, String ownerEmail) {
        Store store = getStoreAndValidateOwner(storeId, ownerEmail);
        storeRepository.delete(store);
    }

    @Override
    @Transactional(readOnly = true)
    public Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Store> getStoresByOwner(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return storeRepository.findByOwner(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Store> getStoresByCuisineType(CUISINE_TYPE cuisineType) {
        return storeRepository.findByCuisineType(cuisineType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Store> searchStoresByName(String name) {
        return storeRepository.findByNameContaining(name);
    }

    private Store getStoreAndValidateOwner(Long storeId, String ownerEmail) {
        Store store = getStoreById(storeId);
        if (!store.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException("해당 매장에 대한 권한이 없습니다.");
        }
        return store;
    }
}