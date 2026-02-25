package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationResponse {
    private Long id;
    private Integer tableId;
    private String tableCode;
    private Long customerId;
    private String customerName;
    private String guestName;
    private String guestPhone;
    private LocalDateTime reservationTime;
    private Integer guestCount;
    private String note;
    private ReservationStatus status;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
}

