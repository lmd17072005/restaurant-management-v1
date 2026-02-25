package com.example.restaurantmanagement.exception;

import lombok.Getter;

/**
 * Custom runtime exception duy nhất của hệ thống.
 * Service sẽ throw AppException(ErrorCode.XYZ) thay vì throw exception thô.
 */
@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}

