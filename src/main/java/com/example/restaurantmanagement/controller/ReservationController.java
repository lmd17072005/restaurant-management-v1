package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.ReservationRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.ReservationResponse;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import com.example.restaurantmanagement.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "Quản lý đặt bàn")
@SecurityRequirement(name = "bearerAuth")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','CUSTOMER')")
    @Operation(summary = "Tạo đặt bàn mới")
    public ResponseEntity<ApiResponse<ReservationResponse>> create(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(reservationService.create(request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    @Operation(summary = "Chi tiết đặt bàn")
    public ResponseEntity<ApiResponse<ReservationResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    @Operation(summary = "Danh sách đặt bàn (phân trang, lọc ngày/trạng thái)")
    public ResponseEntity<ApiResponse<Page<ReservationResponse>>> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) ReservationStatus status,
            @PageableDefault(size = 10, sort = "reservationTime") Pageable pageable) {
        if (from != null && to != null) {
            return ResponseEntity.ok(ApiResponse.success(reservationService.search(from, to, status, pageable)));
        }
        return ResponseEntity.ok(ApiResponse.success(reservationService.getAll(pageable)));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    @Operation(summary = "Lịch sử đặt bàn của khách hàng")
    public ResponseEntity<ApiResponse<Page<ReservationResponse>>> getByCustomer(
            @PathVariable Long customerId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.getByCustomer(customerId, pageable)));
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    @Operation(summary = "Xác nhận đặt bàn")
    public ResponseEntity<ApiResponse<ReservationResponse>> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.confirm(id)));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF','CUSTOMER')")
    @Operation(summary = "Huỷ đặt bàn")
    public ResponseEntity<ApiResponse<ReservationResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.cancel(id)));
    }
}

