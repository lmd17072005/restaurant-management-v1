package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.InvoiceRequest;
import com.example.restaurantmanagement.dto.response.InvoiceResponse;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface InvoiceService {
    InvoiceResponse create(InvoiceRequest request);
    InvoiceResponse getById(Long id);
    Page<InvoiceResponse> getAll(Pageable pageable);
    Page<InvoiceResponse> search(Integer tableId, InvoiceStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable);
    InvoiceResponse getOpenInvoiceByTable(Integer tableId);
    InvoiceResponse recalculate(Long id);
    InvoiceResponse close(Long id);
    InvoiceResponse cancel(Long id);
}

