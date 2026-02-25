package com.example.restaurantmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "danh_muc_mon")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "danh_muc_id")
    private Integer id;

    @Column(name = "ten_danh_muc", nullable = false, unique = true, length = 100)
    private String categoryName;
}
