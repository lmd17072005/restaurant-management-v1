package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.InvoiceRequest;
import com.example.restaurantmanagement.dto.response.InvoiceResponse;
import com.example.restaurantmanagement.entity.Actor;
import com.example.restaurantmanagement.entity.DiningTable;
import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.exception.AppException;
import com.example.restaurantmanagement.exception.ErrorCode;
import com.example.restaurantmanagement.mapper.InvoiceMapper;
import com.example.restaurantmanagement.repository.ActorRepository;
import com.example.restaurantmanagement.repository.DiningTableRepository;
import com.example.restaurantmanagement.repository.InvoiceRepository;
import com.example.restaurantmanagement.repository.OrderItemRepository;
import com.example.restaurantmanagement.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final DiningTableRepository tableRepository;
    private final ActorRepository actorRepository;
    private final OrderItemRepository orderItemRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    @Transactional
    public InvoiceResponse create(InvoiceRequest request) {
        DiningTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_FOUND));

        // Kiểm tra bàn có đang mở hoá đơn khác không
        if (invoiceRepository.findOpenInvoiceByTableId(request.getTableId()).isPresent())
            throw new AppException(ErrorCode.INVOICE_ALREADY_OPEN);

        Actor openedBy = getCurrentActor();
        Actor customer = null;
        if (request.getCustomerId() != null) {
            customer = actorRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new AppException(ErrorCode.ACTOR_NOT_FOUND));
        }

        Invoice invoice = invoiceMapper.toEntity(request);
        invoice.setDiningTable(table);
        invoice.setCustomer(customer);
        invoice.setOpenedBy(openedBy);
        invoice.setStatus(InvoiceStatus.OPEN);
        invoice.setOpenedAt(LocalDateTime.now());
        invoice.setSubtotal(BigDecimal.ZERO);
        invoice.setTotalAmount(BigDecimal.ZERO);

        // Cập nhật trạng thái bàn -> đang phục vụ
        table.setStatus(TableStatus.SERVING);
        tableRepository.save(table);

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getById(Long id) {
        return invoiceMapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getAll(Pageable pageable) {
        return invoiceRepository.findAll(pageable).map(invoiceMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponse> search(Integer tableId, InvoiceStatus status,
                                         LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return invoiceRepository.search(tableId, status, from, to, pageable)
                .map(invoiceMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getOpenInvoiceByTable(Integer tableId) {
        Invoice invoice = invoiceRepository.findOpenInvoiceByTableId(tableId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponse recalculate(Long id) {
        Invoice invoice = findById(id);
        if (invoice.getStatus() != InvoiceStatus.OPEN)
            throw new AppException(ErrorCode.INVOICE_NOT_OPEN);

        // Tính subtotal từ tất cả món chưa huỷ
        BigDecimal subtotal = orderItemRepository.sumLineTotalByInvoiceId(id);
        invoice.setSubtotal(subtotal);

        // total = subtotal - discount + tax + serviceFee
        BigDecimal total = subtotal
                .subtract(invoice.getDiscount())
                .add(invoice.getTax())
                .add(invoice.getServiceFee());
        invoice.setTotalAmount(total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total);

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional
    public InvoiceResponse close(Long id) {
        Invoice invoice = findById(id);
        if (invoice.getStatus() != InvoiceStatus.OPEN)
            throw new AppException(ErrorCode.INVOICE_NOT_OPEN);

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setClosedAt(LocalDateTime.now());

        // Trả bàn về trạng thái trống
        invoice.getDiningTable().setStatus(TableStatus.AVAILABLE);
        tableRepository.save(invoice.getDiningTable());

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional
    public InvoiceResponse cancel(Long id) {
        Invoice invoice = findById(id);
        if (invoice.getStatus() != InvoiceStatus.OPEN)
            throw new AppException(ErrorCode.INVOICE_NOT_OPEN);

        invoice.setStatus(InvoiceStatus.CANCELLED);
        invoice.setClosedAt(LocalDateTime.now());

        invoice.getDiningTable().setStatus(TableStatus.AVAILABLE);
        tableRepository.save(invoice.getDiningTable());

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    // ---- helpers ----
    private Invoice findById(Long id) {
        return invoiceRepository.findWithDetailsById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
    }

    private Actor getCurrentActor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new AppException(ErrorCode.UNAUTHORIZED);
        return actorRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.ACTOR_NOT_FOUND));
    }
}

