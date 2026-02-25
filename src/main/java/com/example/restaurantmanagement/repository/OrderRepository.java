package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Order;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Load invoice + createdBy trong 1 query -> tránh N+1
    @EntityGraph(attributePaths = {"invoice", "invoice.diningTable", "createdBy"})
    Page<Order> findByInvoiceId(Long invoiceId, Pageable pageable);

    @EntityGraph(attributePaths = {"invoice", "invoice.diningTable", "createdBy"})
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    // Lấy tất cả order chưa huỷ của 1 hoá đơn (không phân trang, dùng để tính tiền)
    @Query("""
            SELECT o FROM Order o
            WHERE o.invoice.id = :invoiceId
            AND   o.status <> com.example.restaurantmanagement.entity.enums.OrderStatus.CANCELLED
            ORDER BY o.createdAt ASC
            """)
    List<Order> findActiveByInvoiceId(@Param("invoiceId") Long invoiceId);

    // Tìm kiếm order theo trạng thái + hoá đơn
    @EntityGraph(attributePaths = {"invoice", "invoice.diningTable", "createdBy"})
    @Query("""
            SELECT o FROM Order o
            WHERE (:invoiceId IS NULL OR o.invoice.id = :invoiceId)
            AND   (:status    IS NULL OR o.status     = :status)
            """)
    Page<Order> search(
            @Param("invoiceId") Long invoiceId,
            @Param("status")    OrderStatus status,
            Pageable pageable
    );
}

