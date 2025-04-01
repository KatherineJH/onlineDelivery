package com.deliveryTeam.service.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.deliveryTeam.entity.USER_ROLE;
import com.deliveryTeam.entity.User;
import com.deliveryTeam.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Optional로 변경된 findByEmail을 사용
        Optional<User> userOptional = userRepository.findByEmail(username);
        User user =
                userOptional.orElseThrow(
                        () ->
                                new UsernameNotFoundException(
                                        "User not found with email " + username));

        USER_ROLE role = user.getRole();
        if (role == null) role = USER_ROLE.ROLE_CUSTOMER;

        // GrantedAuthority 리스트 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));

        // UserDetails 객체 반환
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities);
    }
}
