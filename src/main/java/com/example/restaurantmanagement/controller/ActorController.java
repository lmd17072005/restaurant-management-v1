package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.ActorRequest;
import com.example.restaurantmanagement.dto.request.ActorUpdateRequest;
import com.example.restaurantmanagement.dto.response.ActorResponse;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.entity.enums.ActorStatus;
import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.service.ActorService;
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
@RequestMapping("/api/actors")
@RequiredArgsConstructor
@Tag(name = "Actor", description = "Quản lý người dùng (chỉ MANAGER)")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('MANAGER')")
public class ActorController {

    private final ActorService actorService;

    @PostMapping
    @Operation(summary = "Tạo mới người dùng")
    public ResponseEntity<ApiResponse<ActorResponse>> create(@Valid @RequestBody ActorRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(actorService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết người dùng theo ID")
    public ResponseEntity<ApiResponse<ActorResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(actorService.getById(id)));
    }

    @GetMapping
    @Operation(summary = "Danh sách người dùng (phân trang + tìm kiếm)")
    public ResponseEntity<ApiResponse<Page<ActorResponse>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) ActorStatus status,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(actorService.search(keyword, role, status, pageable)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin người dùng")
    public ResponseEntity<ApiResponse<ActorResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ActorUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(actorService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá người dùng")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        actorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xoá thành công", null));
    }
}

