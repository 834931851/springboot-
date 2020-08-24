package com.hyb.mall.exception;

/**
 * 描述：统一异常
 */
public class MallException extends RuntimeException {
    /**
     * 异常码 code
     * 异常信息 msg
     */
    private final Integer code;
    private final String message;

    public MallException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public MallException(MallExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
