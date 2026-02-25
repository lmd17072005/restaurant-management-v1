package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.OrderRequest;
import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.*;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import com.example.restaurantmanagement.entity.enums.OrderItemStatus;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import com.example.restaurantmanagement.exception.AppException;
import com.example.restaurantmanagement.exception.ErrorCode;
import com.example.restaurantmanagement.mapper.OrderMapper;
import com.example.restaurantmanagement.repository.*;
import com.example.restaurantmanagement.service.InvoiceService;
import com.example.restaurantmanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final MenuItemRepository menuItemRepository;
    private final ActorRepository actorRepository;
    private final OrderMapper orderMapper;
    private final InvoiceService invoiceService;

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        if (invoice.getStatus() != InvoiceStatus.OPEN)
            throw new AppException(ErrorCode.INVOICE_NOT_OPEN);

        Actor createdBy = getCurrentActor();

        Order order = new Order();
        order.setInvoice(invoice);
        order.setCreatedBy(createdBy);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.OPEN);
        order.setNote(request.getNote());

        // Tạo danh sách OrderItem từ request
        List<OrderItem> items = new ArrayList<>();
        for (var itemReq : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() -> new AppException(ErrorCode.MENU_ITEM_NOT_FOUND));

            if (menuItem.getStatus() == MenuItemStatus.OUT_OF_STOCK)
                throw new AppException(ErrorCode.MENU_ITEM_UNAVAILABLE,
                        "Món '" + menuItem.getItemName() + "' hiện đã hết");

            BigDecimal unitPrice = menuItem.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMenuItem(menuItem);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setLineTotal(lineTotal);
            item.setStatus(OrderItemStatus.ORDERED);
            item.setItemNote(itemReq.getItemNote());
            items.add(item);
        }
        order.setOrderItems(items);

        Order saved = orderRepository.save(order);

        // Tự động tính lại tổng tiền hoá đơn sau khi thêm đơn hàng
        invoiceService.recalculate(invoice.getId());

        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        return orderMapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getByInvoice(Long invoiceId, Pageable pageable) {
        return orderRepository.findByInvoiceId(invoiceId, pageable).map(orderMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> search(Long invoiceId, OrderStatus status, Pageable pageable) {
        return orderRepository.search(invoiceId, status, pageable).map(orderMapper::toResponse);
    }

    @Override
    @Transactional
    public OrderResponse confirm(Long id) {
        Order order = findById(id);
        if (order.getStatus() != OrderStatus.OPEN)
            throw new AppException(ErrorCode.ORDER_CANNOT_CANCEL, "Chỉ có thể xác nhận đơn hàng ở trạng thái mở");
        order.setStatus(OrderStatus.CONFIRMED);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse cancel(Long id) {
        Order order = findById(id);
        if (order.getStatus() == OrderStatus.SERVED || order.getStatus() == OrderStatus.CANCELLED)
            throw new AppException(ErrorCode.ORDER_CANNOT_CANCEL);

        order.setStatus(OrderStatus.CANCELLED);
        // Huỷ tất cả order item
        order.getOrderItems().forEach(item -> item.setStatus(OrderItemStatus.CANCELLED));

        Order saved = orderRepository.save(order);

        // Tính lại tổng hoá đơn
        invoiceService.recalculate(order.getInvoice().getId());

        return orderMapper.toResponse(saved);
    }

    // ---- helpers ----
    private Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    private Actor getCurrentActor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new AppException(ErrorCode.UNAUTHORIZED);
        return actorRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.ACTOR_NOT_FOUND));
    }
}

