package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.DiningTableRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.DiningTableResponse;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.service.DiningTableService;
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
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@Tag(name = "DiningTable", description = "Quản lý bàn ăn")
public class DiningTableController {

    private final DiningTableService tableService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Tạo bàn mới")
    public ResponseEntity<ApiResponse<DiningTableResponse>> create(@Valid @RequestBody DiningTableRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(tableService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết bàn")
    public ResponseEntity<ApiResponse<DiningTableResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(tableService.getById(id)));
    }

    @GetMapping
    @Operation(summary = "Danh sách bàn (phân trang, lọc trạng thái)")
    public ResponseEntity<ApiResponse<Page<DiningTableResponse>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TableStatus status,
            @PageableDefault(size = 10, sort = "tableCode") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(tableService.search(keyword, status, pageable)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cập nhật thông tin bàn")
    public ResponseEntity<ApiResponse<DiningTableResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody DiningTableRequest request) {
        return ResponseEntity.ok(ApiResponse.success(tableService.update(id, request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cập nhật trạng thái bàn")
    public ResponseEntity<ApiResponse<DiningTableResponse>> updateStatus(
            @PathVariable Integer id,
            @RequestParam TableStatus status) {
        return ResponseEntity.ok(ApiResponse.success(tableService.updateStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Xoá bàn")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        tableService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xoá thành công", null));
    }
}

