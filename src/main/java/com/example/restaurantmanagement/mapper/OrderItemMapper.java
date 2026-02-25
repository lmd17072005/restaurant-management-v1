package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.response.OrderItemResponse;
import com.example.restaurantmanagement.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "orderId",      source = "order.id")
    @Mapping(target = "menuItemId",   source = "menuItem.id")
    @Mapping(target = "menuItemName", source = "menuItem.itemName")
    @Mapping(target = "categoryName", source = "menuItem.category.categoryName")
    OrderItemResponse toResponse(OrderItem orderItem);
}

