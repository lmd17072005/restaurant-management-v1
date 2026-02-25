package com.example.restaurantmanagement.entity.converter;

import com.example.restaurantmanagement.entity.enums.OrderItemStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderItemStatusConverter extends BaseEnumConverter<OrderItemStatus> {
    public OrderItemStatusConverter() { super(OrderItemStatus.class); }
}