package com.example.restaurantmanagement.entity.converter;

import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InvoiceStatusConverter extends BaseEnumConverter<InvoiceStatus> {
    public InvoiceStatusConverter() { super(InvoiceStatus.class); }
}