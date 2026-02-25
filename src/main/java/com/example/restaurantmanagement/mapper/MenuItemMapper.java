package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;
import com.example.restaurantmanagement.entity.MenuItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    // categoryId -> category sẽ được Service set thủ công
    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "status",   expression = "java(request.getStatus() != null ? request.getStatus() : com.example.restaurantmanagement.entity.enums.MenuItemStatus.AVAILABLE)")
    MenuItem toEntity(MenuItemRequest request);

    // Flatten: lấy category.id và category.categoryName ra field phẳng
    @Mapping(target = "categoryId",   source = "category.id")
    @Mapping(target = "categoryName", source = "category.categoryName")
    MenuItemResponse toResponse(MenuItem menuItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntity(MenuItemRequest request, @MappingTarget MenuItem menuItem);
}

