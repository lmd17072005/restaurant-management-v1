package com.example.restaurantmanagement.entity.enums;

public enum ActorStatus implements DbValueEnum {
    ACTIVE("hoat_dong"),
    INACTIVE("ngung_hoat_dong");

    private final String dbValue;
    ActorStatus(String dbValue) { this.dbValue = dbValue; }

    @Override
    public String getDbValue() { return dbValue; }
}
