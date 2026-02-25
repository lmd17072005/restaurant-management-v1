package com.example.restaurantmanagement.entity.enums;

public enum InvoiceStatus implements DbValueEnum {
    OPEN("dang_mo"),
    PAID("da_thanh_toan"),
    CANCELLED("huy");

    private final String dbValue;
    InvoiceStatus(String dbValue) { this.dbValue = dbValue; }

    @Override
    public String getDbValue() { return dbValue; }
}
