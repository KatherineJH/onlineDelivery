package com.deliveryTeam.dto;

import com.deliveryTeam.entity.USER_ROLE;
import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private USER_ROLE role;
}
