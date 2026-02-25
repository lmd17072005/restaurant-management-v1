package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.MenuCategoryRequest;
import com.example.restaurantmanagement.dto.response.MenuCategoryResponse;
import com.example.restaurantmanagement.entity.MenuCategory;
import com.example.restaurantmanagement.exception.AppException;
import com.example.restaurantmanagement.exception.ErrorCode;
import com.example.restaurantmanagement.mapper.MenuCategoryMapper;
import com.example.restaurantmanagement.repository.MenuCategoryRepository;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.service.MenuCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuCategoryServiceImpl implements MenuCategoryService {

    private final MenuCategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryMapper categoryMapper;

    @Override
    @Transactional
    public MenuCategoryResponse create(MenuCategoryRequest request) {
        if (categoryRepository.existsByCategoryName(request.getCategoryName()))
            throw new AppException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS);

        MenuCategory category = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuCategoryResponse getById(Integer id) {
        return categoryMapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuCategoryResponse> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuCategoryResponse> search(String keyword, Pageable pageable) {
        return categoryRepository
                .findByCategoryNameContainingIgnoreCase(keyword, pageable)
                .map(categoryMapper::toResponse);
    }

    @Override
    @Transactional
    public MenuCategoryResponse update(Integer id, MenuCategoryRequest request) {
        MenuCategory category = findById(id);

        if (!category.getCategoryName().equals(request.getCategoryName())
                && categoryRepository.existsByCategoryName(request.getCategoryName()))
            throw new AppException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS);

        categoryMapper.updateEntity(request, category);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        MenuCategory category = findById(id);

        // Không cho xoá nếu còn món ăn thuộc danh mục này
        if (menuItemRepository.existsByCategoryId(id))
            throw new AppException(ErrorCode.CATEGORY_HAS_ITEMS);

        categoryRepository.delete(category);
    }

    private MenuCategory findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}

