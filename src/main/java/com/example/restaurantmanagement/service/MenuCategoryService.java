package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.MenuCategoryRequest;
import com.example.restaurantmanagement.dto.response.MenuCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuCategoryService {
    MenuCategoryResponse create(MenuCategoryRequest request);
    MenuCategoryResponse getById(Integer id);
    Page<MenuCategoryResponse> getAll(Pageable pageable);
    Page<MenuCategoryResponse> search(String keyword, Pageable pageable);
    MenuCategoryResponse update(Integer id, MenuCategoryRequest request);
    void delete(Integer id);
}

