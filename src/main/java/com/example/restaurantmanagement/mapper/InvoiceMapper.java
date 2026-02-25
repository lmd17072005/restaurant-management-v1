package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.request.InvoiceRequest;
import com.example.restaurantmanagement.dto.response.InvoiceResponse;
import com.example.restaurantmanagement.entity.Invoice;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "diningTable", ignore = true)
    @Mapping(target = "customer",    ignore = true)
    @Mapping(target = "openedBy",    ignore = true)
    @Mapping(target = "openedAt",    ignore = true)
    @Mapping(target = "closedAt",    ignore = true)
    @Mapping(target = "subtotal",    ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "status",      ignore = true)
    @Mapping(target = "orders",      ignore = true)
    Invoice toEntity(InvoiceRequest request);

    @Mapping(target = "tableId",      source = "diningTable.id")
    @Mapping(target = "tableCode",    source = "diningTable.tableCode")
    @Mapping(target = "customerId",   source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "openedById",   source = "openedBy.id")
    @Mapping(target = "openedByName", source = "openedBy.fullName")
    InvoiceResponse toResponse(Invoice invoice);
}

