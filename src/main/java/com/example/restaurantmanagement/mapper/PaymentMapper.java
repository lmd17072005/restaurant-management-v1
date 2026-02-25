package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "invoiceId",       source = "invoice.id")
    @Mapping(target = "tableCode",       source = "invoice.diningTable.tableCode")
    @Mapping(target = "processedById",   source = "processedBy.id")
    @Mapping(target = "processedByName", source = "processedBy.fullName")
    PaymentResponse toResponse(Payment payment);
}

