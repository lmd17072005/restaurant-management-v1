package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private Long actorId;
    private String username;
    private Role role;
}

