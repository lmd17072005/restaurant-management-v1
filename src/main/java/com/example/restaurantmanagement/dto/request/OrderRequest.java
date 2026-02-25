package com.example.restaurantmanagement.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    @NotNull(message = "Hoá đơn không được để trống")
    private Long invoiceId;

    @NotEmpty(message = "Đơn hàng phải có ít nhất 1 món")
    @Valid
    private List<OrderItemRequest> items;

    @Size(max = 255, message = "Ghi chú tối đa 255 ký tự")
    private String note;
}

