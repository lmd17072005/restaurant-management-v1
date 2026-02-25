package com.example.restaurantmanagement.entity.converter;

import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MenuItemStatusConverter extends BaseEnumConverter<MenuItemStatus> {
    public MenuItemStatusConverter() { super(MenuItemStatus.class); }
}