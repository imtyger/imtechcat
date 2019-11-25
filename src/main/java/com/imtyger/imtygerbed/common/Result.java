package com.imtyger.imtygerbed.common;

/**
 * @Author: imtygerx@gmail.com
 * @Date: 2019/11/24
 */

public class Result<T extends Object> {

    // 错误码
    private Integer code;

    // 错误信息、说明描述等
    private String msg;

    // 封装返回数据
    private T data;

    /**
     * 仅返回成功状态，无返回数据时使用
     *
     * @param code 错误码
     */
    public Result(Code code) {
        if (code == null) {
            throw new NullPointerException("状态码为空");
        }
        this.code = code.getValue();
    }

    /**
     * 仅返回成功状态及数据结果
     *
     * @param code 错误码
     * @param data 返回数据
     */
    public Result(Code code, T data) {
        this(code);
        this.data = data;
    }

    /**
     * 返回错误码，错误说明信息，数据结果
     *
     * @param code    错误码
     * @param msg 错误信息，描述信息
     * @param data    返回数据
     */
    public Result(Code code, String msg, T data) {
        this(code, data);
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // =====================================================================

    // 成功
    public final static Code SUCCESS = new Code(200);
    // 失败
    public final static Code FAIL = new Code(400);
    // 未认证
    public final static Code UNAUTHORIZED = new Code(401);
    // 无权限
    public final static Code NO_PERMISSION = new Code(402);

    // 普通成功仅返回状态
    public static Result<?> success() {
        return new Result<>(SUCCESS, null);
    }

    // 返回成功状态及描述信息
    public static Result<?> success(String msg) {
        return new Result<>(SUCCESS, msg, null);
    }

    // 仅返回成功数据
    public static Result<?> success(Object data) {
        return new Result<>(SUCCESS, data);
    }

    // 成功
    public static Result<?> success(String msg, Object data) {
        return new Result<>(SUCCESS, msg, data);
    }

    // 失败
    public static Result<?> fail(String msg) {
        return new Result<>(FAIL, msg, null);
    }

    // 未认证
    public static Result<?> unauthorized () {
        return new Result<>(UNAUTHORIZED, "暂未登录或token已经过期", null);
    }

    // 无权限
    public static Result<?> noPermission() {
        return new Result<>(FAIL, "请授权或登录其他账号", null);
    }

    /**
     * 错误码
     */
    public static class Code {

        private int value;

        public Code(int value) {
            super();
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
