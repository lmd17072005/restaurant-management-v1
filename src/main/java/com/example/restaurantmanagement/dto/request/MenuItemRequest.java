package com.example.restaurantmanagement.dto.request;

import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuItemRequest {

    @NotNull(message = "Danh mục không được để trống")
    private Integer categoryId;

    @NotBlank(message = "Tên món không được để trống")
    @Size(max = 150, message = "Tên món tối đa 150 ký tự")
    private String itemName;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    private MenuItemStatus status;

    private String description;
}

