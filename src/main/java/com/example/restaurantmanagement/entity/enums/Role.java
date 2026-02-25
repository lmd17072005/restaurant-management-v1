package com.example.restaurantmanagement.entity.enums;

public enum Role implements DbValueEnum {
    MANAGER("QUAN_LY"),
    STAFF("NHAN_VIEN"),
    CUSTOMER("KHACH_HANG");

    private final String dbValue;
    Role(String dbValue) { this.dbValue = dbValue; }

    @Override
    public String getDbValue() { return dbValue; }
}
