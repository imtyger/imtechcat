package com.imtyger.imtygerbed.common.exception;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * Created by 910888783@qq.com on 2019/11/18.
 */
public class BusinessException extends RuntimeException{

    private Integer code = 0;
    private String msg = StringUtils.EMPTY;

    public BusinessException(Integer code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(Exception e) {
        super(e);
        if (e instanceof BusinessException) {
            this.code = ((BusinessException) e).getCode();
            this.msg = e.getMessage();
        }
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
