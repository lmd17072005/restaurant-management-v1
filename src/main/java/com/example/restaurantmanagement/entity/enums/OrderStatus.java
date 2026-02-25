package com.example.restaurantmanagement.entity.enums;

public enum OrderStatus implements DbValueEnum {
    OPEN("mo"),
    CONFIRMED("xac_nhan"),
    SERVING("dang_phuc_vu"),
    SERVED("da_phuc_vu"),
    CANCELLED("huy");

    private final String dbValue;
    OrderStatus(String dbValue) { this.dbValue = dbValue; }

    @Override
    public String getDbValue() { return dbValue; }
}
