package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dat_ban")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dat_ban_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ban_id", nullable = false)
    private DiningTable diningTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id")
    private Actor customer;

    @Column(name = "ten_khach", length = 100)
    private String guestName;

    @Column(name = "sdt_khach", length = 20)
    private String guestPhone;

    @Column(name = "thoi_gian_dat", nullable = false)
    private LocalDateTime reservationTime;

    @Column(name = "so_nguoi", nullable = false)
    private Integer guestCount;

    @Column(name = "ghi_chu", length = 255)
    private String note;

    @Column(name = "trang_thai", nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tao_boi", nullable = false)
    private Actor createdBy;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = ReservationStatus.PENDING;
    }
}