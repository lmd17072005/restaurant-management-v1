package com.example.restaurantmanagement.entity.converter;

import com.example.restaurantmanagement.entity.enums.PaymentMethod;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentMethodConverter extends BaseEnumConverter<PaymentMethod> {
    public PaymentMethodConverter() { super(PaymentMethod.class); }
}