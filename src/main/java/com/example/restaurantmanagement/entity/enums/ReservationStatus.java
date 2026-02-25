package com.example.restaurantmanagement.entity.enums;

public enum ReservationStatus implements DbValueEnum {
    PENDING("cho_xac_nhan"),
    CONFIRMED("da_dat"),
    CANCELLED("huy");

    private final String dbValue;
    ReservationStatus(String dbValue) { this.dbValue = dbValue; }

    @Override
    public String getDbValue() { return dbValue; }
}