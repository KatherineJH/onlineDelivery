package com.deliveryTeam.service;

import com.deliveryTeam.entity.Address;
import com.deliveryTeam.http.response.Response;

public interface AddressService {
//    Response saveAndUpdateAddress(Address address);

    Response saveAndUpdateAddress(Long userId, Address address);
}
