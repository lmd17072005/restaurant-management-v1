package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuItemService {
    MenuItemResponse create(MenuItemRequest request);
    MenuItemResponse getById(Integer id);
    Page<MenuItemResponse> getAll(Pageable pageable);
    Page<MenuItemResponse> search(String keyword, Integer categoryId, MenuItemStatus status, Pageable pageable);
    MenuItemResponse update(Integer id, MenuItemRequest request);
    void delete(Integer id);
}

