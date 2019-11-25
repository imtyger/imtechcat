package com.imtyger.imtygerbed.exception;

/**
 * @Author: imtygerx@gmail.com
 * @Date: 2019/11/24
 */

public class BusinessException extends RuntimeException {
    private Integer code;

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public Integer getCode() {
        return code;
    }
}
