package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.PaymentRequest;
import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.entity.Actor;
import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.Payment;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import com.example.restaurantmanagement.exception.AppException;
import com.example.restaurantmanagement.exception.ErrorCode;
import com.example.restaurantmanagement.mapper.PaymentMapper;
import com.example.restaurantmanagement.repository.ActorRepository;
import com.example.restaurantmanagement.repository.InvoiceRepository;
import com.example.restaurantmanagement.repository.PaymentRepository;
import com.example.restaurantmanagement.service.InvoiceService;
import com.example.restaurantmanagement.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final ActorRepository actorRepository;
    private final PaymentMapper paymentMapper;
    private final InvoiceService invoiceService;

    @Override
    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        // Kiểm tra hoá đơn tồn tại và đang mở
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
        if (invoice.getStatus() != InvoiceStatus.OPEN)
            throw new AppException(ErrorCode.INVOICE_NOT_OPEN);

        // Kiểm tra hoá đơn chưa có thanh toán (1 lần duy nhất)
        if (paymentRepository.existsByInvoiceId(request.getInvoiceId()))
            throw new AppException(ErrorCode.PAYMENT_ALREADY_EXISTS);

        // Kiểm tra mã giao dịch trùng (nếu có)
        if (request.getTransactionCode() != null
                && paymentRepository.existsByTransactionCode(request.getTransactionCode()))
            throw new AppException(ErrorCode.TRANSACTION_CODE_ALREADY_EXISTS);

        // Tính lại tổng trước khi thanh toán để đảm bảo số tiền chính xác
        invoiceService.recalculate(invoice.getId());
        // Reload invoice sau recalculate
        invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        // Kiểm tra số tiền thanh toán khớp tổng hoá đơn
        if (request.getAmount().compareTo(invoice.getTotalAmount()) != 0)
            throw new AppException(ErrorCode.PAYMENT_AMOUNT_MISMATCH,
                    "Số tiền thanh toán (" + request.getAmount()
                            + ") không khớp tổng hoá đơn (" + invoice.getTotalAmount() + ")");

        Actor processedBy = getCurrentActor();

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(request.getAmount());
        payment.setPaidAt(LocalDateTime.now());
        payment.setProcessedBy(processedBy);
        payment.setTransactionCode(request.getTransactionCode());

        Payment saved = paymentRepository.save(payment);

        // Đóng hoá đơn sau khi thanh toán thành công
        invoiceService.close(invoice.getId());

        return paymentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getById(Long id) {
        return paymentMapper.toResponse(
                paymentRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getByInvoice(Long invoiceId) {
        return paymentMapper.toResponse(
                paymentRepository.findByInvoiceId(invoiceId)
                        .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAll(Pageable pageable) {
        return paymentRepository.findAll(pageable).map(paymentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponse> search(com.example.restaurantmanagement.entity.enums.PaymentMethod paymentMethod,
                                         LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return paymentRepository.search(paymentMethod, from, to, pageable).map(paymentMapper::toResponse);
    }

    private Actor getCurrentActor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new AppException(ErrorCode.UNAUTHORIZED);
        return actorRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.ACTOR_NOT_FOUND));
    }
}

