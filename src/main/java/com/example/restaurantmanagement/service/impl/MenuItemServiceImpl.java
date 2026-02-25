package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;
import com.example.restaurantmanagement.entity.MenuCategory;
import com.example.restaurantmanagement.entity.MenuItem;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import com.example.restaurantmanagement.exception.AppException;
import com.example.restaurantmanagement.exception.ErrorCode;
import com.example.restaurantmanagement.mapper.MenuItemMapper;
import com.example.restaurantmanagement.repository.MenuCategoryRepository;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository categoryRepository;
    private final MenuItemMapper menuItemMapper;

    @Override
    @Transactional
    public MenuItemResponse create(MenuItemRequest request) {
        MenuCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        MenuItem item = menuItemMapper.toEntity(request);
        item.setCategory(category);
        if (item.getStatus() == null) item.setStatus(MenuItemStatus.AVAILABLE);

        return menuItemMapper.toResponse(menuItemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse getById(Integer id) {
        return menuItemMapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponse> getAll(Pageable pageable) {
        return menuItemRepository.findAll(pageable).map(menuItemMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponse> search(String keyword, Integer categoryId, MenuItemStatus status, Pageable pageable) {
        return menuItemRepository.search(keyword, categoryId, status, pageable)
                .map(menuItemMapper::toResponse);
    }

    @Override
    @Transactional
    public MenuItemResponse update(Integer id, MenuItemRequest request) {
        MenuItem item = findById(id);

        MenuCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        menuItemMapper.updateEntity(request, item);
        item.setCategory(category);

        return menuItemMapper.toResponse(menuItemRepository.save(item));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!menuItemRepository.existsById(id))
            throw new AppException(ErrorCode.MENU_ITEM_NOT_FOUND);
        menuItemRepository.deleteById(id);
    }

    private MenuItem findById(Integer id) {
        return menuItemRepository.findWithCategoryById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MENU_ITEM_NOT_FOUND));
    }
}

