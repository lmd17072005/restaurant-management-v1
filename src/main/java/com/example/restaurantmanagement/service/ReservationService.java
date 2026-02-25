package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.ReservationRequest;
import com.example.restaurantmanagement.dto.response.ReservationResponse;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReservationService {
    ReservationResponse create(ReservationRequest request);
    ReservationResponse getById(Long id);
    Page<ReservationResponse> getAll(Pageable pageable);
    Page<ReservationResponse> search(LocalDateTime from, LocalDateTime to, ReservationStatus status, Pageable pageable);
    Page<ReservationResponse> getByCustomer(Long customerId, Pageable pageable);
    ReservationResponse confirm(Long id);
    ReservationResponse cancel(Long id);
}

