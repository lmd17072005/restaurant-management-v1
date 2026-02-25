package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.DiningTableRequest;
import com.example.restaurantmanagement.dto.response.DiningTableResponse;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiningTableService {
    DiningTableResponse create(DiningTableRequest request);
    DiningTableResponse getById(Integer id);
    Page<DiningTableResponse> getAll(Pageable pageable);
    Page<DiningTableResponse> search(String keyword, TableStatus status, Pageable pageable);
    DiningTableResponse update(Integer id, DiningTableRequest request);
    DiningTableResponse updateStatus(Integer id, TableStatus status);
    void delete(Integer id);
}

