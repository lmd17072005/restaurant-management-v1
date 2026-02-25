package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.ActorStatus;
import com.example.restaurantmanagement.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ActorResponse {
    private Long id;
    private Role role;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String username;
    private ActorStatus status;
    private LocalDateTime createdAt;
}

