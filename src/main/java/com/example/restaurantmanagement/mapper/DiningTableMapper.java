package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.request.DiningTableRequest;
import com.example.restaurantmanagement.dto.response.DiningTableResponse;
import com.example.restaurantmanagement.entity.DiningTable;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DiningTableMapper {

    @Mapping(target = "id",     ignore = true)
    @Mapping(target = "status", ignore = true)
    DiningTable toEntity(DiningTableRequest request);

    DiningTableResponse toResponse(DiningTable table);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",     ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntity(DiningTableRequest request, @MappingTarget DiningTable table);
}

