package com.example.restaurantmanagement.entity.converter;

import com.example.restaurantmanagement.entity.enums.Role;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter extends BaseEnumConverter<Role> {
    public RoleConverter() { super(Role.class); }
}