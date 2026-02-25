package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuItemResponse {
    private Integer id;
    private Integer categoryId;
    private String categoryName;
    private String itemName;
    private BigDecimal price;
    private MenuItemStatus status;
    private String description;
}

