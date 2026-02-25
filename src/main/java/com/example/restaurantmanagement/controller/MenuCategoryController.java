package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.MenuCategoryRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.MenuCategoryResponse;
import com.example.restaurantmanagement.service.MenuCategoryService;
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
@RequestMapping("/api/menu-categories")
@RequiredArgsConstructor
@Tag(name = "MenuCategory", description = "Quản lý danh mục món ăn")
public class MenuCategoryController {

    private final MenuCategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Tạo danh mục mới")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> create(@Valid @RequestBody MenuCategoryRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(categoryService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết danh mục")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getById(id)));
    }

    @GetMapping
    @Operation(summary = "Danh sách danh mục (phân trang)")
    public ResponseEntity<ApiResponse<Page<MenuCategoryResponse>>> getAll(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "categoryName") Pageable pageable) {
        if (keyword != null && !keyword.isBlank()) {
            return ResponseEntity.ok(ApiResponse.success(categoryService.search(keyword, pageable)));
        }
        return ResponseEntity.ok(ApiResponse.success(categoryService.getAll(pageable)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cập nhật danh mục")
    public ResponseEntity<ApiResponse<MenuCategoryResponse>> update(
            @PathVariable Integer id,
            @Valid @RequestBody MenuCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Xoá danh mục (chặn nếu còn món)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xoá thành công", null));
    }
}

