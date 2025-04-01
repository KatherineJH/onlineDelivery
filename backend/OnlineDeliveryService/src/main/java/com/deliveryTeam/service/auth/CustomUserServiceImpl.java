package com.deliveryTeam.service.auth;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.deliveryTeam.entity.User;
import com.deliveryTeam.repository.UserRepository;
import com.deliveryTeam.security.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements CustomUserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElseThrow(() -> new Exception("User not found with email: " + email));
    }
}
