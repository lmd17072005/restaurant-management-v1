package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ban_an")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id")
    private Integer id;

    @Column(name = "ma_ban", nullable = false, unique = true, length = 10)
    private String tableCode;

    @Column(name = "suc_chua", nullable = false)
    private Integer capacity;

    @Column(name = "trang_thai", nullable = false)
    private TableStatus status = TableStatus.AVAILABLE;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = TableStatus.AVAILABLE;
    }
}