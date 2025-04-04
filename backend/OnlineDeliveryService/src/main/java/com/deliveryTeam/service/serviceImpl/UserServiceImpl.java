package com.deliveryTeam.service.serviceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliveryTeam.entity.Cart;
import com.deliveryTeam.entity.OrderEntity;
import com.deliveryTeam.entity.USER_ROLE;
import com.deliveryTeam.entity.User;
import com.deliveryTeam.repository.CartRepository;
import com.deliveryTeam.repository.UserRepository;
import com.deliveryTeam.service.UserService;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        try {
            // 비밀번호 암호화
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // 1. 장바구니 생성 (ROLE_CUSTOMER인 경우)
            if (user.getRole() == USER_ROLE.ROLE_CUSTOMER) {
                Cart cart = new Cart();
                cart.setTotalPrice(BigDecimal.ZERO);
                user.setCart(cart);
                cart.setUser(user);
            }

            // 2. 사용자와 장바구니 함께 저장 (CascadeType.ALL 덕분에 자동으로 처리됨)
            User savedUser = userRepository.saveAndFlush(user);
            entityManager.clear(); // 영속성 컨텍스트 초기화

            // 3. 저장된 사용자 다시 조회
            return userRepository
                    .findById(savedUser.getUserId())
                    .orElseThrow(() -> new RuntimeException("사용자 저장 실패"));

        } catch (Exception e) {
            throw new RuntimeException("사용자 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Override
    public User login(String email, String password) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<OrderEntity> getUserOrders(Long userId) {
        User user = getUserById(userId);
        return user.getOrders();
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = getUserById(userId);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
