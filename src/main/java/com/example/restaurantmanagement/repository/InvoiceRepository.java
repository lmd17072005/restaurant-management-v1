package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Load diningTable + customer + openedBy trong 1 query -> tránh N+1
    @EntityGraph(attributePaths = {"diningTable", "customer", "openedBy"})
    Page<Invoice> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"diningTable", "customer", "openedBy"})
    Optional<Invoice> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"diningTable", "customer", "openedBy"})
    Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"diningTable", "customer", "openedBy"})
    Page<Invoice> findByCustomerId(Long customerId, Pageable pageable);

    // Tìm hoá đơn đang mở của một bàn cụ thể (mỗi bàn chỉ có 1 hoá đơn đang mở)
    @Query("""
            SELECT i FROM Invoice i
            WHERE i.diningTable.id = :tableId
            AND   i.status = com.example.restaurantmanagement.entity.enums.InvoiceStatus.OPEN
            """)
    Optional<Invoice> findOpenInvoiceByTableId(@Param("tableId") Integer tableId);

    // Lấy hoá đơn đã thanh toán theo khoảng ngày (dùng cho báo cáo doanh thu)
    @EntityGraph(attributePaths = {"diningTable", "customer", "openedBy"})
    @Query("""
            SELECT i FROM Invoice i
            WHERE i.closedAt BETWEEN :from AND :to
            AND   i.status = com.example.restaurantmanagement.entity.enums.InvoiceStatus.PAID
            """)
    Page<Invoice> findPaidByDateRange(
            @Param("from") LocalDateTime from,
            @Param("to")   LocalDateTime to,
            Pageable pageable
    );

    // Tìm kiếm đa điều kiện: bàn + trạng thái + khoảng ngày
    @EntityGraph(attributePaths = {"diningTable", "customer", "openedBy"})
    @Query("""
            SELECT i FROM Invoice i
            WHERE (:tableId IS NULL OR i.diningTable.id = :tableId)
            AND   (:status  IS NULL OR i.status         = :status)
            AND   (:from    IS NULL OR i.openedAt       >= :from)
            AND   (:to      IS NULL OR i.openedAt       <= :to)
            """)
    Page<Invoice> search(
            @Param("tableId") Integer tableId,
            @Param("status")  InvoiceStatus status,
            @Param("from")    LocalDateTime from,
            @Param("to")      LocalDateTime to,
            Pageable pageable
    );
}

