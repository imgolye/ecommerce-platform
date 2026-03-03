package com.b2b2c.common.core.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Integer code;

    public BusinessException(String message) {
        this(400, message);
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
