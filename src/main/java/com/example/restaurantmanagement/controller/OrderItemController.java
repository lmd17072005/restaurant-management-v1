package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.OrderItemResponse;
import com.example.restaurantmanagement.entity.enums.OrderItemStatus;
import com.example.restaurantmanagement.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@Tag(name = "OrderItem", description = "Quản lý chi tiết món (bếp)")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('MANAGER','STAFF')")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết món trong đơn")
    public ResponseEntity<ApiResponse<OrderItemResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderItemService.getById(id)));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Danh sách món theo đơn hàng")
    public ResponseEntity<ApiResponse<Page<OrderItemResponse>>> getByOrder(
            @PathVariable Long orderId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(orderItemService.getByOrder(orderId, pageable)));
    }

    @GetMapping("/kitchen/pending")
    @Operation(summary = "Màn hình bếp: danh sách món đang chờ/đang nấu")
    public ResponseEntity<ApiResponse<List<OrderItemResponse>>> getPendingForKitchen() {
        return ResponseEntity.ok(ApiResponse.success(orderItemService.getPendingForKitchen()));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Cập nhật trạng thái món (bếp xác nhận nấu/hoàn thành)")
    public ResponseEntity<ApiResponse<OrderItemResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderItemStatus status) {
        return ResponseEntity.ok(ApiResponse.success(orderItemService.updateStatus(id, status)));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Huỷ món")
    public ResponseEntity<ApiResponse<OrderItemResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderItemService.cancel(id)));
    }
}

