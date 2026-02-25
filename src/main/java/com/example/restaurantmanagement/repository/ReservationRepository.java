package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Reservation;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Load diningTable + customer + createdBy trong 1 query -> tránh N+1
    @EntityGraph(attributePaths = {"diningTable", "customer", "createdBy"})
    Page<Reservation> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"diningTable", "customer", "createdBy"})
    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"diningTable", "customer", "createdBy"})
    Page<Reservation> findByCustomerId(Long customerId, Pageable pageable);

    @EntityGraph(attributePaths = {"diningTable", "customer", "createdBy"})
    Page<Reservation> findByDiningTableId(Integer tableId, Pageable pageable);

    // Kiểm tra xung đột đặt bàn trong khoảng thời gian
    @Query("""
            SELECT COUNT(r) > 0 FROM Reservation r
            WHERE r.diningTable.id = :tableId
            AND   r.status <> com.example.restaurantmanagement.entity.enums.ReservationStatus.CANCELLED
            AND   r.reservationTime BETWEEN :from AND :to
            """)
    boolean existsConflictingReservation(
            @Param("tableId") Integer tableId,
            @Param("from")    LocalDateTime from,
            @Param("to")      LocalDateTime to
    );

    // Tìm kiếm theo khoảng ngày + trạng thái, load các quan hệ luôn
    @EntityGraph(attributePaths = {"diningTable", "customer", "createdBy"})
    @Query("""
            SELECT r FROM Reservation r
            WHERE r.reservationTime BETWEEN :from AND :to
            AND   (:status IS NULL OR r.status = :status)
            """)
    Page<Reservation> findByDateRangeAndStatus(
            @Param("from")   LocalDateTime from,
            @Param("to")     LocalDateTime to,
            @Param("status") ReservationStatus status,
            Pageable pageable
    );
}

