package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.OrderRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import com.example.restaurantmanagement.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Quản lý đơn hàng")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('MANAGER','STAFF')")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Tạo đơn hàng mới (gọi món)")
    public ResponseEntity<ApiResponse<OrderResponse>> create(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(orderService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết đơn hàng")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getById(id)));
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Danh sách đơn hàng theo hoá đơn")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getByInvoice(
            @PathVariable Long invoiceId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getByInvoice(invoiceId, pageable)));
    }

    @GetMapping
    @Operation(summary = "Danh sách đơn hàng (phân trang, lọc)")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> search(
            @RequestParam(required = false) Long invoiceId,
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(orderService.search(invoiceId, status, pageable)));
    }

    @PatchMapping("/{id}/confirm")
    @Operation(summary = "Xác nhận đơn hàng")
    public ResponseEntity<ApiResponse<OrderResponse>> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.confirm(id)));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Huỷ đơn hàng")
    public ResponseEntity<ApiResponse<OrderResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.cancel(id)));
    }
}

