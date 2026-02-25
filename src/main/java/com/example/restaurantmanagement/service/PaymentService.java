package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.PaymentRequest;
import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.entity.enums.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PaymentService {
    PaymentResponse create(PaymentRequest request);
    PaymentResponse getById(Long id);
    PaymentResponse getByInvoice(Long invoiceId);
    Page<PaymentResponse> getAll(Pageable pageable);
    Page<PaymentResponse> search(PaymentMethod paymentMethod, LocalDateTime from, LocalDateTime to, Pageable pageable);
}

