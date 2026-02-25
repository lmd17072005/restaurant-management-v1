package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.ActorStatus;
import com.example.restaurantmanagement.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tac_nhan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tac_nhan_id")
    private Long id;

    @Column(name = "vai_tro", nullable = false)
    private Role role;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String fullName;

    @Column(name = "so_dien_thoai", unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "ten_dang_nhap", unique = true, length = 50)
    private String username;

    @Column(name = "mat_khau_hash", length = 255)
    private String passwordHash;

    @Column(name = "trang_thai", nullable = false)
    private ActorStatus status = ActorStatus.ACTIVE;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = ActorStatus.ACTIVE;
    }
}