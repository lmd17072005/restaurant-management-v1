package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import com.example.restaurantmanagement.service.MenuItemService;
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
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
@Tag(name = "MenuItem", description = "Quản lý món ăn")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Thêm món ăn mới")
    public ResponseEntity<ApiResponse<MenuItemResponse>> create(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(menuItemService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết món ăn")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getById(id)));
    }

    @GetMapping
    @Operation(summary = "Danh sách món ăn (phân trang, tìm kiếm, lọc danh mục/trạng thái)")
    public ResponseEntity<ApiResponse<Page<MenuItemResponse>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) MenuItemStatus status,
            @PageableDefault(size = 10, sort = "itemName") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
                menuItemService.search(keyword, categoryId, status, pageable)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cập nhật món ăn")
    public ResponseEntity<ApiResponse<MenuItemResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Xoá món ăn")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        menuItemService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xoá thành công", null));
    }
}

