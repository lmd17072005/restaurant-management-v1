package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.OrderItem;
import com.example.restaurantmanagement.entity.enums.OrderItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Load menuItem + category trong 1 query -> tránh N+1
    @EntityGraph(attributePaths = {"menuItem", "menuItem.category"})
    Page<OrderItem> findByOrderId(Long orderId, Pageable pageable);

    // Dành cho màn hình bếp: lấy các món đang chờ/đang nấu, kèm thông tin bàn
    @EntityGraph(attributePaths = {"menuItem", "order", "order.invoice", "order.invoice.diningTable"})
    @Query("""
            SELECT oi FROM OrderItem oi
            WHERE oi.status IN (
                com.example.restaurantmanagement.entity.enums.OrderItemStatus.ORDERED,
                com.example.restaurantmanagement.entity.enums.OrderItemStatus.COOKING
            )
            ORDER BY oi.order.createdAt ASC
            """)
    List<OrderItem> findPendingForKitchen();

    // Lọc theo trạng thái, có phân trang
    @EntityGraph(attributePaths = {"menuItem", "menuItem.category", "order"})
    Page<OrderItem> findByStatus(OrderItemStatus status, Pageable pageable);

    // Tính tổng tiền các món chưa huỷ của 1 hoá đơn (tránh load toàn bộ entity)
    @Query("""
            SELECT COALESCE(SUM(oi.lineTotal), 0) FROM OrderItem oi
            WHERE oi.order.invoice.id = :invoiceId
            AND   oi.status <> com.example.restaurantmanagement.entity.enums.OrderItemStatus.CANCELLED
            """)
    BigDecimal sumLineTotalByInvoiceId(@Param("invoiceId") Long invoiceId);

    // Tìm kiếm đa điều kiện: theo order + trạng thái
    @EntityGraph(attributePaths = {"menuItem", "menuItem.category"})
    @Query("""
            SELECT oi FROM OrderItem oi
            WHERE (:orderId IS NULL OR oi.order.id = :orderId)
            AND   (:status  IS NULL OR oi.status   = :status)
            """)
    Page<OrderItem> search(
            @Param("orderId") Long orderId,
            @Param("status")  OrderItemStatus status,
            Pageable pageable
    );
}

