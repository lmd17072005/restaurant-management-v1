package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.request.MenuCategoryRequest;
import com.example.restaurantmanagement.dto.response.MenuCategoryResponse;
import com.example.restaurantmanagement.entity.MenuCategory;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MenuCategoryMapper {

    @Mapping(target = "id", ignore = true)
    MenuCategory toEntity(MenuCategoryRequest request);

    MenuCategoryResponse toResponse(MenuCategory category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(MenuCategoryRequest request, @MappingTarget MenuCategory category);
}

