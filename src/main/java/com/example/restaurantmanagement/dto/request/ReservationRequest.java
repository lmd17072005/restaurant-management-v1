package com.example.restaurantmanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequest {

    @NotNull(message = "Bàn không được để trống")
    private Integer tableId;

    private Long customerId;

    @Size(max = 100, message = "Tên khách tối đa 100 ký tự")
    private String guestName;

    @Size(max = 20, message = "Số điện thoại tối đa 20 ký tự")
    private String guestPhone;

    @NotNull(message = "Thời gian đặt bàn không được để trống")
    @Future(message = "Thời gian đặt bàn phải là thời điểm trong tương lai")
    private LocalDateTime reservationTime;

    @NotNull(message = "Số người không được để trống")
    @Min(value = 1, message = "Số người tối thiểu là 1")
    private Integer guestCount;

    @Size(max = 255, message = "Ghi chú tối đa 255 ký tự")
    private String note;
}

