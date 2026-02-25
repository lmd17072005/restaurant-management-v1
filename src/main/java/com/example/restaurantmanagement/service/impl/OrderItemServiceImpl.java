package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.response.OrderItemResponse;
import com.example.restaurantmanagement.entity.OrderItem;
import com.example.restaurantmanagement.entity.enums.OrderItemStatus;
import com.example.restaurantmanagement.exception.AppException;
import com.example.restaurantmanagement.exception.ErrorCode;
import com.example.restaurantmanagement.mapper.OrderItemMapper;
import com.example.restaurantmanagement.repository.OrderItemRepository;
import com.example.restaurantmanagement.service.InvoiceService;
import com.example.restaurantmanagement.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final InvoiceService invoiceService;

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponse getById(Long id) {
        return orderItemMapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemResponse> getByOrder(Long orderId, Pageable pageable) {
        return orderItemRepository.findByOrderId(orderId, pageable).map(orderItemMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponse> getPendingForKitchen() {
        return orderItemRepository.findPendingForKitchen()
                .stream().map(orderItemMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public OrderItemResponse updateStatus(Long id, OrderItemStatus status) {
        OrderItem item = findById(id);
        if (item.getStatus() == OrderItemStatus.CANCELLED)
            throw new AppException(ErrorCode.ORDER_ITEM_CANNOT_CANCEL, "Không thể cập nhật món đã huỷ");
        item.setStatus(status);
        return orderItemMapper.toResponse(orderItemRepository.save(item));
    }

    @Override
    @Transactional
    public OrderItemResponse cancel(Long id) {
        OrderItem item = findById(id);
        if (item.getStatus() == OrderItemStatus.SERVED
                || item.getStatus() == OrderItemStatus.CANCELLED)
            throw new AppException(ErrorCode.ORDER_ITEM_CANNOT_CANCEL);

        item.setStatus(OrderItemStatus.CANCELLED);
        OrderItem saved = orderItemRepository.save(item);

        // Tính lại tổng hoá đơn sau khi huỷ món
        invoiceService.recalculate(item.getOrder().getInvoice().getId());

        return orderItemMapper.toResponse(saved);
    }

    private OrderItem findById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_ITEM_NOT_FOUND));
    }
}

