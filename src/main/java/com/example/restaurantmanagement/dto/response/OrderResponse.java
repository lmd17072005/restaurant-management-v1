package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private Long invoiceId;
    private String tableCode;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String note;
    private List<OrderItemResponse> items;
}

