package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.TableStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiningTableResponse {
    private Integer id;
    private String tableCode;
    private Integer capacity;
    private TableStatus status;
}

