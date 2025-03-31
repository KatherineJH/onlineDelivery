package com.deliveryTeam.service.user;

import com.deliveryTeam.dto.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO login(String email, String password);
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}
