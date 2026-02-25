package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.InvoiceRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.InvoiceResponse;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import com.example.restaurantmanagement.service.InvoiceService;
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
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoice", description = "Quản lý hoá đơn")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('MANAGER','STAFF')")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    @Operation(summary = "Mở hoá đơn mới cho bàn")
    public ResponseEntity<ApiResponse<InvoiceResponse>> create(@Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(invoiceService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết hoá đơn")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getById(id)));
    }

    @GetMapping
    @Operation(summary = "Danh sách hoá đơn (phân trang, lọc)")
    public ResponseEntity<ApiResponse<Page<InvoiceResponse>>> getAll(
            @RequestParam(required = false) Integer tableId,
            @RequestParam(required = false) InvoiceStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @PageableDefault(size = 10, sort = "openedAt") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.search(tableId, status, from, to, pageable)));
    }

    @GetMapping("/table/{tableId}/open")
    @Operation(summary = "Lấy hoá đơn đang mở của một bàn")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getOpenByTable(@PathVariable Integer tableId) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getOpenInvoiceByTable(tableId)));
    }

    @PatchMapping("/{id}/recalculate")
    @Operation(summary = "Tính lại tổng tiền hoá đơn")
    public ResponseEntity<ApiResponse<InvoiceResponse>> recalculate(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.recalculate(id)));
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "Đóng hoá đơn (đánh dấu đã thanh toán)")
    public ResponseEntity<ApiResponse<InvoiceResponse>> close(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.close(id)));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Huỷ hoá đơn (chỉ MANAGER)")
    public ResponseEntity<ApiResponse<InvoiceResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.cancel(id)));
    }
}

