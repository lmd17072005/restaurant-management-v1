package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.OrderItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "chi_tiet_don_hang")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ct_don_hang_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "don_hang_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_id", nullable = false)
    private MenuItem menuItem;

    @Column(name = "so_luong", nullable = false)
    private Integer quantity;

    @Column(name = "don_gia", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "thanh_tien", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;

    @Column(name = "trang_thai", nullable = false)
    private OrderItemStatus status = OrderItemStatus.ORDERED;

    @Column(name = "ghi_chu_mon", length = 255)
    private String itemNote;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = OrderItemStatus.ORDERED;
    }
}