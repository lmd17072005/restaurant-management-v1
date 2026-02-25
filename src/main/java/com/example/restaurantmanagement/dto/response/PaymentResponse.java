package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponse {
    private Long id;
    private Long invoiceId;
    private String tableCode;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private LocalDateTime paidAt;
    private Long processedById;
    private String processedByName;
    private String transactionCode;
}
