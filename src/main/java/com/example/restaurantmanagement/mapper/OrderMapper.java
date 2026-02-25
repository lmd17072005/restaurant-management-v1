package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "invoiceId",     source = "invoice.id")
    @Mapping(target = "tableCode",     source = "invoice.diningTable.tableCode")
    @Mapping(target = "createdById",   source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.fullName")
    @Mapping(target = "items",         source = "orderItems")
    OrderResponse toResponse(Order order);
}

