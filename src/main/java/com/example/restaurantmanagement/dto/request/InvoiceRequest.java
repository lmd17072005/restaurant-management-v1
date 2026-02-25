package com.example.restaurantmanagement.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvoiceRequest {

    @NotNull(message = "Bàn không được để trống")
    private Integer tableId;

    private Long customerId;

    @DecimalMin(value = "0.0", message = "Giảm giá không được âm")
    private BigDecimal discount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Tiền thuế không được âm")
    private BigDecimal tax = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Phí dịch vụ không được âm")
    private BigDecimal serviceFee = BigDecimal.ZERO;
}

