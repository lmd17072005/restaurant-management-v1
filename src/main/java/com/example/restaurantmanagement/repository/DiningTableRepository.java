package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.DiningTable;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, Integer> {

    Optional<DiningTable> findByTableCode(String tableCode);

    boolean existsByTableCode(String tableCode);

    // Lọc theo trạng thái, có phân trang
    Page<DiningTable> findByStatus(TableStatus status, Pageable pageable);

    // Tìm bàn trống có sức chứa tối thiểu
    Page<DiningTable> findByStatusAndCapacityGreaterThanEqual(
            TableStatus status,
            Integer minCapacity,
            Pageable pageable
    );

    // Tìm kiếm theo mã bàn, lọc theo trạng thái
    @Query("""
            SELECT t FROM DiningTable t
            WHERE (:keyword IS NULL
                OR LOWER(t.tableCode) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:status IS NULL OR t.status = :status)
            """)
    Page<DiningTable> search(
            @Param("keyword") String keyword,
            @Param("status")  TableStatus status,
            Pageable pageable
    );
}

