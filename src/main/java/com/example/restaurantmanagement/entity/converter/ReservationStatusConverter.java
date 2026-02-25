package com.example.restaurantmanagement.entity.converter;


import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ReservationStatusConverter extends BaseEnumConverter<ReservationStatus> {
    public ReservationStatusConverter() { super(ReservationStatus.class); }
}