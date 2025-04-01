package com.deliveryTeam.service.auth;

import com.deliveryTeam.entity.User;

public interface CustomUserService {

    User findUserByJwtToken(String token) throws Exception;

    User findUserByEmail(String email) throws Exception;
}
