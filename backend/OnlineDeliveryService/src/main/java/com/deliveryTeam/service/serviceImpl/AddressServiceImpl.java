package com.deliveryTeam.service.serviceImpl;

import com.deliveryTeam.entity.Address;
import com.deliveryTeam.entity.Store;
import com.deliveryTeam.entity.User;
import com.deliveryTeam.http.response.Response;
import com.deliveryTeam.repository.AddressRepository;
import com.deliveryTeam.service.AddressService;
import com.deliveryTeam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    /**
     * 나중에 사용해야 함
     * */
//    @Override
//    public Response saveAndUpdateAddress(Address address) {
//        User user = userService.getLoginUser();
//
//        // 사용자가 이미 주소를 가지고 있는지 확인
//        Address existingAddress = user.getAddress();
//        if (existingAddress == null) {
//            existingAddress = new Address();
//            existingAddress.setUser(user);
//        }
//
//        // 새로운 주소 정보 업데이트 (주소 정보가 null이 아닌 경우에만 업데이트)
//        if (address.getStreetName() != null) existingAddress.setStreetName(address.getStreetName());
//        if (address.getCity() != null) existingAddress.setCity(address.getCity());
//        if (address.getState() != null) existingAddress.setState(address.getState());
//        if (address.getZipCode() != null) existingAddress.setZipCode(address.getZipCode());
//
//        addressRepository.save(existingAddress);
//
//        String message = (user.getAddress() == null) ? "Address successfully created" : "Address successfully updated";
//        return Response.builder()
//                .status(200)
//                .message(message)
//                .build();
//
//        String message = "Address successfully saved";
//        return Response.builder()
//                .status(200)
//                .message(message)
//                .build();
//    }

    @Override
    public Response saveAndUpdateAddress(Long userId, Address address) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return Response.builder()
                    .status(404)
                    .message("User not found")
                    .build();
        }

        address.setUser(user);            // 사용자 주소 설정
        address.setStore(null);            // 상점 주소는 null로 설정
        address.setAddressType(Address.AddressType.USER);  // 주소 타입 USER로 설정
        addressRepository.save(address);

        return Response.builder()
                .status(200)
                .message("User Address successfully saved")
                .address(address)
                .build();
    }

    // 상점 주소 등록 (주석 처리)
    /*
    public Response saveStoreAddress(Long storeId, Address address) {
        Store store = storeService.getStoreById(storeId);
        if (store == null) {
            return Response.builder()
                    .status(404)
                    .message("Store not found")
                    .build();
        }

        address.setStore(store);           // 상점 주소 설정
        address.setUser(null);             // 사용자 주소는 null로 설정
        address.setAddressType(Address.AddressType.STORE);  // 주소 타입 STORE로 설정
        addressRepository.save(address);

        return Response.builder()
                .status(200)
                .message("Store Address successfully saved")
                .address(address)
                .build();
    }
    */

}
