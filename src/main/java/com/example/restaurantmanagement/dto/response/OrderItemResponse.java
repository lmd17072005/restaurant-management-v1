package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.OrderItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {
    private Long id;
    private Long orderId;
    private Integer menuItemId;
    private String menuItemName;
    private String categoryName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private OrderItemStatus status;
    private String itemNote;
}

