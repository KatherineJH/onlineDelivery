package com.deliveryTeam.service;

import java.util.List;

import com.deliveryTeam.entity.OrderEntity;
import com.deliveryTeam.entity.User;

public interface UserService {
    User registerUser(User user);

    User login(String email, String password);

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    List<OrderEntity> getUserOrders(Long userId);

    void changePassword(Long userId, String currentPassword, String newPassword);
}
