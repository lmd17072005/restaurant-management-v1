package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.request.ActorRequest;
import com.example.restaurantmanagement.dto.request.ActorUpdateRequest;
import com.example.restaurantmanagement.dto.response.ActorResponse;
import com.example.restaurantmanagement.entity.Actor;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ActorMapper {

    // Request -> Entity (bỏ qua passwordHash vì Service sẽ encode riêng)
    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "status",       ignore = true)
    @Mapping(target = "createdAt",    ignore = true)
    Actor toEntity(ActorRequest request);

    // Entity -> Response (KHÔNG trả passwordHash)
    ActorResponse toResponse(Actor actor);

    // Cập nhật entity từ UpdateRequest, bỏ qua field null
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "role",         ignore = true)
    @Mapping(target = "username",     ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt",    ignore = true)
    void updateEntity(ActorUpdateRequest request, @MappingTarget Actor actor);
}

