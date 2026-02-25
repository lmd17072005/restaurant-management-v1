package com.example.restaurantmanagement.entity.enums;

public enum MenuItemStatus implements DbValueEnum {
    AVAILABLE("con_ban"),
    OUT_OF_STOCK("het_mon");

    private final String dbValue;
    MenuItemStatus(String dbValue) { this.dbValue = dbValue; }

    @Override
    public String getDbValue() { return dbValue; }
}
