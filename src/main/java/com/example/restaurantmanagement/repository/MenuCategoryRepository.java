package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.MenuCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Integer> {

    Optional<MenuCategory> findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);

    // Tìm kiếm theo tên danh mục, có phân trang
    Page<MenuCategory> findByCategoryNameContainingIgnoreCase(String keyword, Pageable pageable);
}

