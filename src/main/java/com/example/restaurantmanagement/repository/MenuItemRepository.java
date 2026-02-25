package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.MenuItem;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    // @EntityGraph để JOIN FETCH category trong 1 query -> tránh N+1
    @EntityGraph(attributePaths = {"category"})
    Optional<MenuItem> findWithCategoryById(Integer id);

    // Lấy danh sách theo danh mục, load category luôn -> tránh N+1
    @EntityGraph(attributePaths = {"category"})
    Page<MenuItem> findByCategoryId(Integer categoryId, Pageable pageable);

    // Lấy danh sách theo trạng thái, load category luôn
    @EntityGraph(attributePaths = {"category"})
    Page<MenuItem> findByStatus(MenuItemStatus status, Pageable pageable);

    // Tìm kiếm đa điều kiện: keyword + categoryId + status
    @EntityGraph(attributePaths = {"category"})
    @Query("""
            SELECT m FROM MenuItem m
            WHERE (:keyword    IS NULL OR LOWER(m.itemName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND   (:categoryId IS NULL OR m.category.id = :categoryId)
            AND   (:status     IS NULL OR m.status      = :status)
            """)
    Page<MenuItem> search(
            @Param("keyword")    String keyword,
            @Param("categoryId") Integer categoryId,
            @Param("status")     MenuItemStatus status,
            Pageable pageable
    );

    // Đếm số món theo danh mục (dùng khi xoá category)
    boolean existsByCategoryId(Integer categoryId);
}

