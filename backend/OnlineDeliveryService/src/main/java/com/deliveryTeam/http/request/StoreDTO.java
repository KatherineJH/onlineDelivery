package com.deliveryTeam.http.request;

import com.deliveryTeam.entity.CUISINE_TYPE;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDTO {
    private String name;
    private String location;
    private CUISINE_TYPE cuisineType;
    private String streetName;
    private String city;
    private String state;
    private String zipCode;
}
