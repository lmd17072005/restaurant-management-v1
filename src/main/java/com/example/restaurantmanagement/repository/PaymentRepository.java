package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Payment;
import com.example.restaurantmanagement.entity.enums.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Load invoice + processedBy trong 1 query -> tránh N+1
    @EntityGraph(attributePaths = {"invoice", "invoice.diningTable", "processedBy"})
    Page<Payment> findAll(Pageable pageable);

    // Mỗi hoá đơn chỉ có 1 lần thanh toán -> trả về Optional
    @EntityGraph(attributePaths = {"invoice", "processedBy"})
    Optional<Payment> findByInvoiceId(Long invoiceId);

    // Kiểm tra hoá đơn đã được thanh toán chưa (dùng ở Service trước khi tạo Payment)
    boolean existsByInvoiceId(Long invoiceId);

    // Tìm theo mã giao dịch (unique)
    Optional<Payment> findByTransactionCode(String transactionCode);

    boolean existsByTransactionCode(String transactionCode);

    // Lọc theo phương thức thanh toán, có phân trang
    @EntityGraph(attributePaths = {"invoice", "invoice.diningTable", "processedBy"})
    Page<Payment> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable);

    // Lọc theo nhân viên xử lý, có phân trang
    @EntityGraph(attributePaths = {"invoice", "invoice.diningTable", "processedBy"})
    Page<Payment> findByProcessedById(Long processedById, Pageable pageable);

    // Tính tổng doanh thu theo khoảng thời gian (aggregate query, không load entity)
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0) FROM Payment p
            WHERE p.paidAt BETWEEN :from AND :to
            """)
    BigDecimal sumAmountByDateRange(
            @Param("from") LocalDateTime from,
            @Param("to")   LocalDateTime to
    );

    // Thống kê doanh thu nhóm theo phương thức thanh toán
    @Query("""
            SELECT p.paymentMethod, COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.paidAt BETWEEN :from AND :to
            GROUP BY p.paymentMethod
            """)
    List<Object[]> sumAmountGroupByMethod(
            @Param("from") LocalDateTime from,
            @Param("to")   LocalDateTime to
    );

    // Tìm kiếm đa điều kiện: khoảng ngày + phương thức thanh toán
    @EntityGraph(attributePaths = {"invoice", "invoice.diningTable", "processedBy"})
    @Query("""
            SELECT p FROM Payment p
            WHERE (:paymentMethod IS NULL OR p.paymentMethod = :paymentMethod)
            AND   (:from          IS NULL OR p.paidAt        >= :from)
            AND   (:to            IS NULL OR p.paidAt        <= :to)
            """)
    Page<Payment> search(
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("from")          LocalDateTime from,
            @Param("to")            LocalDateTime to,
            Pageable pageable
    );
}

