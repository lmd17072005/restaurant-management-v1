package com.example.restaurantmanagement.entity.enums;

public enum PaymentMethod implements DbValueEnum {
    CASH("tien_mat"),
    CARD("the"),
    BANK_TRANSFER("chuyen_khoan"),
    E_WALLET("vi_dien_tu");

    private final String dbValue;
    PaymentMethod(String dbValue) { this.dbValue = dbValue; }

    @Override
    public String getDbValue() { return dbValue; }
}