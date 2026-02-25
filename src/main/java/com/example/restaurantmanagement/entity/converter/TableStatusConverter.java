package com.example.restaurantmanagement.entity.converter;

import com.example.restaurantmanagement.entity.enums.TableStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TableStatusConverter extends BaseEnumConverter<TableStatus> {
    public TableStatusConverter() { super(TableStatus.class); }
}