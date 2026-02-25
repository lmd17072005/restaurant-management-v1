package com.example.restaurantmanagement.entity.enums;

public enum OrderItemStatus implements DbValueEnum {
    ORDERED("da_goi"),
    COOKING("dang_nau"),
    SERVED("da_len"),
    CANCELLED("huy");

    private final String dbValue;
    OrderItemStatus(String dbValue) { this.dbValue = dbValue; }

    @Override
    public String getDbValue() { return dbValue; }
}
