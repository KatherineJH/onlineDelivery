package com.deliveryTeam.service.user;

import com.deliveryTeam.entity.User;
import java.util.List;

public interface UserService {
    User registerUser(User user);
    User login(String email, String password);
    User getUserById(Long id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
