package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.request.ReservationRequest;
import com.example.restaurantmanagement.dto.response.ReservationResponse;
import com.example.restaurantmanagement.entity.Reservation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    // diningTable, customer, createdBy sẽ được Service set thủ công
    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "diningTable", ignore = true)
    @Mapping(target = "customer",    ignore = true)
    @Mapping(target = "createdBy",   ignore = true)
    @Mapping(target = "status",      ignore = true)
    @Mapping(target = "createdAt",   ignore = true)
    Reservation toEntity(ReservationRequest request);

    // Flatten các quan hệ lồng nhau ra field phẳng
    @Mapping(target = "tableId",       source = "diningTable.id")
    @Mapping(target = "tableCode",     source = "diningTable.tableCode")
    @Mapping(target = "customerId",    source = "customer.id")
    @Mapping(target = "customerName",  source = "customer.fullName")
    @Mapping(target = "createdById",   source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.fullName")
    ReservationResponse toResponse(Reservation reservation);
}

