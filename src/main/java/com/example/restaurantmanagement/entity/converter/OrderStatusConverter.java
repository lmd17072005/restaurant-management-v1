package com.example.restaurantmanagement.entity.converter;

import com.example.restaurantmanagement.entity.enums.OrderStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter extends BaseEnumConverter<OrderStatus> {
    public OrderStatusConverter() { super(OrderStatus.class); }
}