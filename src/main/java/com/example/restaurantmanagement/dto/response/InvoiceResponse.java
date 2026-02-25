package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class InvoiceResponse {
    private Long id;
    private Integer tableId;
    private String tableCode;
    private Long customerId;
    private String customerName;
    private Long openedById;
    private String openedByName;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal serviceFee;
    private BigDecimal totalAmount;
    private InvoiceStatus status;
}

