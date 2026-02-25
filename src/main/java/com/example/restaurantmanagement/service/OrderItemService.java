package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.response.OrderItemResponse;
import com.example.restaurantmanagement.entity.enums.OrderItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderItemService {
    OrderItemResponse getById(Long id);
    Page<OrderItemResponse> getByOrder(Long orderId, Pageable pageable);
    List<OrderItemResponse> getPendingForKitchen();
    OrderItemResponse updateStatus(Long id, OrderItemStatus status);
    OrderItemResponse cancel(Long id);
}

