package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hoa_don")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hoa_don_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ban_id", nullable = false)
    private DiningTable diningTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id")
    private Actor customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mo_boi", nullable = false)
    private Actor openedBy;

    @Column(name = "ngay_mo", nullable = false, updatable = false)
    private LocalDateTime openedAt;

    @Column(name = "ngay_dong")
    private LocalDateTime closedAt;

    @Column(name = "tam_tinh", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "giam_gia", nullable = false, precision = 12, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "tien_thue", nullable = false, precision = 12, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(name = "phi_dich_vu", nullable = false, precision = 12, scale = 2)
    private BigDecimal serviceFee = BigDecimal.ZERO;

    @Column(name = "tong_tien", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "trang_thai", nullable = false)
    private InvoiceStatus status = InvoiceStatus.OPEN;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (openedAt == null) openedAt = LocalDateTime.now();
        if (status == null) status = InvoiceStatus.OPEN;
    }
}