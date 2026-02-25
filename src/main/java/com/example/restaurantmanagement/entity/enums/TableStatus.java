package com.example.restaurantmanagement.entity.enums;

public enum TableStatus implements DbValueEnum {
    AVAILABLE("trong"),
    SERVING("dang_phuc_vu"),
    RESERVED("da_dat"),
    MAINTENANCE("bao_tri");

    private final String dbValue;
    TableStatus(String dbValue) { this.dbValue = dbValue; }

    @Override
    public String getDbValue() { return dbValue; }
}
