package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.ActorRequest;
import com.example.restaurantmanagement.dto.request.ActorUpdateRequest;
import com.example.restaurantmanagement.dto.response.ActorResponse;
import com.example.restaurantmanagement.entity.enums.ActorStatus;
import com.example.restaurantmanagement.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActorService {
    ActorResponse create(ActorRequest request);
    ActorResponse getById(Long id);
    Page<ActorResponse> getAll(Pageable pageable);
    Page<ActorResponse> search(String keyword, Role role, ActorStatus status, Pageable pageable);
    ActorResponse update(Long id, ActorUpdateRequest request);
    void delete(Long id);
}

