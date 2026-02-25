package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.OrderRequest;
import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse create(OrderRequest request);
    OrderResponse getById(Long id);
    Page<OrderResponse> getByInvoice(Long invoiceId, Pageable pageable);
    Page<OrderResponse> search(Long invoiceId, OrderStatus status, Pageable pageable);
    OrderResponse confirm(Long id);
    OrderResponse cancel(Long id);
}

