package com.example.restaurantmanagement.entity.converter;

import com.example.restaurantmanagement.entity.enums.ActorStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ActorStatusConverter extends BaseEnumConverter<ActorStatus> {
    public ActorStatusConverter() { super(ActorStatus.class); }
}