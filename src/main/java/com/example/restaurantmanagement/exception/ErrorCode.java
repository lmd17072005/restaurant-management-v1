package com.example.restaurantmanagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enum định nghĩa tất cả mã lỗi của hệ thống.
 * Mỗi error code gắn với 1 HTTP status và message mặc định.
 */
@Getter
public enum ErrorCode {

    // ===== COMMON =====
    INTERNAL_SERVER_ERROR(5000, "Lỗi hệ thống, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_FAILED(4000, "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(4004, "Không tìm thấy tài nguyên", HttpStatus.NOT_FOUND),
    ACCESS_DENIED(4003, "Bạn không có quyền thực hiện thao tác này", HttpStatus.FORBIDDEN),
    UNAUTHORIZED(4001, "Vui lòng đăng nhập để tiếp tục", HttpStatus.UNAUTHORIZED),

    // ===== ACTOR =====
    ACTOR_NOT_FOUND(1001, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    USERNAME_ALREADY_EXISTS(1002, "Tên đăng nhập đã tồn tại", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS(1003, "Email đã tồn tại", HttpStatus.CONFLICT),
    PHONE_ALREADY_EXISTS(1004, "Số điện thoại đã tồn tại", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS(1005, "Tên đăng nhập hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED),
    ACTOR_INACTIVE(1006, "Tài khoản đã bị vô hiệu hoá", HttpStatus.FORBIDDEN),

    // ===== DINING TABLE =====
    TABLE_NOT_FOUND(2001, "Không tìm thấy bàn", HttpStatus.NOT_FOUND),
    TABLE_CODE_ALREADY_EXISTS(2002, "Mã bàn đã tồn tại", HttpStatus.CONFLICT),
    TABLE_NOT_AVAILABLE(2003, "Bàn hiện không khả dụng", HttpStatus.BAD_REQUEST),

    // ===== MENU CATEGORY =====
    CATEGORY_NOT_FOUND(3001, "Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_ALREADY_EXISTS(3002, "Tên danh mục đã tồn tại", HttpStatus.CONFLICT),
    CATEGORY_HAS_ITEMS(3003, "Danh mục còn chứa món ăn, không thể xoá", HttpStatus.BAD_REQUEST),

    // ===== MENU ITEM =====
    MENU_ITEM_NOT_FOUND(4001, "Không tìm thấy món ăn", HttpStatus.NOT_FOUND),
    MENU_ITEM_UNAVAILABLE(4002, "Món ăn hiện đã hết", HttpStatus.BAD_REQUEST),

    // ===== RESERVATION =====
    RESERVATION_NOT_FOUND(5001, "Không tìm thấy đặt bàn", HttpStatus.NOT_FOUND),
    RESERVATION_CONFLICT(5002, "Bàn đã được đặt trong khung giờ này", HttpStatus.CONFLICT),
    RESERVATION_CANNOT_CANCEL(5003, "Không thể huỷ đặt bàn ở trạng thái hiện tại", HttpStatus.BAD_REQUEST),

    // ===== INVOICE =====
    INVOICE_NOT_FOUND(6001, "Không tìm thấy hoá đơn", HttpStatus.NOT_FOUND),
    INVOICE_ALREADY_OPEN(6002, "Bàn này đang có hoá đơn mở", HttpStatus.CONFLICT),
    INVOICE_NOT_OPEN(6003, "Hoá đơn không ở trạng thái mở", HttpStatus.BAD_REQUEST),
    INVOICE_ALREADY_PAID(6004, "Hoá đơn đã được thanh toán", HttpStatus.BAD_REQUEST),

    // ===== ORDER =====
    ORDER_NOT_FOUND(7001, "Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND),
    ORDER_CANNOT_CANCEL(7002, "Không thể huỷ đơn hàng ở trạng thái hiện tại", HttpStatus.BAD_REQUEST),

    // ===== ORDER ITEM =====
    ORDER_ITEM_NOT_FOUND(8001, "Không tìm thấy chi tiết đơn hàng", HttpStatus.NOT_FOUND),
    ORDER_ITEM_CANNOT_CANCEL(8002, "Không thể huỷ món ăn ở trạng thái hiện tại", HttpStatus.BAD_REQUEST),

    // ===== PAYMENT =====
    PAYMENT_NOT_FOUND(9001, "Không tìm thấy thanh toán", HttpStatus.NOT_FOUND),
    PAYMENT_ALREADY_EXISTS(9002, "Hoá đơn này đã được thanh toán", HttpStatus.CONFLICT),
    TRANSACTION_CODE_ALREADY_EXISTS(9003, "Mã giao dịch đã tồn tại", HttpStatus.CONFLICT),
    PAYMENT_AMOUNT_MISMATCH(9004, "Số tiền thanh toán không khớp với tổng hoá đơn", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

