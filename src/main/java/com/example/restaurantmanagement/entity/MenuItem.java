package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "mon_an")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mon_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danh_muc_id", nullable = false)
    private MenuCategory category;

    @Column(name = "ten_mon", nullable = false, length = 150)
    private String itemName;

    @Column(name = "gia", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "trang_thai", nullable = false)
    private MenuItemStatus status = MenuItemStatus.AVAILABLE;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String description;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = MenuItemStatus.AVAILABLE;
    }
}