package com.deliveryTeam.service.user;

import java.util.List;

import com.deliveryTeam.entity.User;

public interface UserService {
    User registerUser(User user);

    User login(String email, String password);

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
