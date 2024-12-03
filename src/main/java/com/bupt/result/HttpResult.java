package com.bupt.result;


import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhaoDesong
 * @date 2020/10/29 17:08
 */
@Data
public final class HttpResult<T> implements Serializable {

    private final int code;
    private final String msg;
    private final T data;

    public HttpResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <Result> HttpResult<Result> of() {
        return new HttpResult<>(HttpResultCodeEnum.SUCCESS.getCode(), HttpResultCodeEnum.SUCCESS.getMsg(), null);
    }

    public static <Result> HttpResult<Result> of(Result data) {
        return new HttpResult<>(HttpResultCodeEnum.SUCCESS.getCode(), HttpResultCodeEnum.SUCCESS.getMsg(), data);
    }

    public static <Result> HttpResult<Result> of(HttpResultCodeEnum result,Result data) {
        return new HttpResult<>(result.getCode(), result.getMsg(), data);
    }
    public static <Result> HttpResult<Result> of(HttpResultCodeEnum result) {
        return new HttpResult<>(result.getCode(), result.getMsg(), null);
    }

    public static <Result> HttpResult<Result> of(int code) {
        return new HttpResult<>(code, null, null);
    }

    public static <Result> HttpResult<Result> of(int code, String msg) {
        return new HttpResult<>(code, msg, null);
    }

    public static <Result> HttpResult<Result> of(int code, String msg, Result data) {
        return new HttpResult<>(code, msg, data);
    }


}
