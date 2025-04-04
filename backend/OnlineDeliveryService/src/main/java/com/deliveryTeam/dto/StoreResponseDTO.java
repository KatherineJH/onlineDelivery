package com.deliveryTeam.dto;

import com.deliveryTeam.entity.CUISINE_TYPE;
import com.deliveryTeam.entity.Store;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreResponseDTO {
    private Long storeId;
    private String name;
    private String location;
    private CUISINE_TYPE cuisineType;
    private String ownerName;
    private AddressDTO address;

    @Getter
    @Setter
    public static class AddressDTO {
        private String streetName;
        private String city;
        private String state;
        private String zipCode;
    }

    public static StoreResponseDTO from(Store store) {
        StoreResponseDTO dto = new StoreResponseDTO();
        dto.setStoreId(store.getStoreId());
        dto.setName(store.getName());
        dto.setLocation(store.getLocation());
        dto.setCuisineType(store.getCuisineType());
        dto.setOwnerName(store.getOwner().getUsername());

        if (store.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreetName(store.getAddress().getStreetName());
            addressDTO.setCity(store.getAddress().getCity());
            addressDTO.setState(store.getAddress().getState());
            addressDTO.setZipCode(store.getAddress().getZipCode());
            dto.setAddress(addressDTO);
        }

        return dto;
    }
}