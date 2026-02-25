package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.PaymentRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.entity.enums.PaymentMethod;
import com.example.restaurantmanagement.service.PaymentService;
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
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Quản lý thanh toán")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('MANAGER','STAFF')")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Tạo thanh toán cho hoá đơn")
    public ResponseEntity<ApiResponse<PaymentResponse>> create(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(paymentService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết thanh toán")
    public ResponseEntity<ApiResponse<PaymentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getById(id)));
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Lấy thanh toán theo hoá đơn")
    public ResponseEntity<ApiResponse<PaymentResponse>> getByInvoice(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getByInvoice(invoiceId)));
    }

    @GetMapping
    @Operation(summary = "Danh sách thanh toán (phân trang, lọc ngày/phương thức)")
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getAll(
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @PageableDefault(size = 10, sort = "paidAt") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.search(paymentMethod, from, to, pageable)));
    }
}

