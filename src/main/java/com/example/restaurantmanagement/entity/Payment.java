package com.example.restaurantmanagement.entity;


import com.example.restaurantmanagement.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thanh_toan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id", nullable = false)
    private Invoice invoice;

    @Column(name = "phuong_thuc", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "so_tien", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "thoi_gian_thanh_toan", nullable = false, updatable = false)
    private LocalDateTime paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xu_ly_boi", nullable = false)
    private Actor processedBy;

    @Column(name = "ma_giao_dich", length = 100)
    private String transactionCode;

    @PrePersist
    protected void onCreate() {
        if (paidAt == null) paidAt = LocalDateTime.now();
    }
}