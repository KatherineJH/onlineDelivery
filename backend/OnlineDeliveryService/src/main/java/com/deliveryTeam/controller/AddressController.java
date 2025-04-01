package com.deliveryTeam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deliveryTeam.entity.Address;
import com.deliveryTeam.http.response.Response;
import com.deliveryTeam.service.AddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    //    @PostMapping("/save")
    //    public ResponseEntity<Response> saveAndUpdateAddress(@RequestBody Address address){
    //        return ResponseEntity.ok(addressService.saveAndUpdateAddress(address));
    //    }

    /** 임시 컨트롤러, Not to be used in the end */
    // 사용자 주소 등록
    @PostMapping("/user/{userId}")
    public ResponseEntity<Response> saveUserAddress(
            @PathVariable Long userId, @RequestBody Address address) {
        return ResponseEntity.ok(addressService.saveAndUpdateAddress(userId, address));
    }

    /*
    @PostMapping("/store/{storeId}")
    public ResponseEntity<Response> saveStoreAddress(@PathVariable Long storeId, @RequestBody Address address) {
        return ResponseEntity.ok(addressService.saveStoreAddress(storeId, address));
    }
    */
}
