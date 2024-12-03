package com.bupt.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaokaiHttpResult<T> implements Serializable {

    private  int errcode;
    private  String errmsg;
    private  T data;

    public BaokaiHttpResult(int errcode, String errmsg, T data) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.data = data;
    }
    public BaokaiHttpResult(T data) {
        this.errcode = 0;
        this.errmsg = "";
        this.data = data;
    }

    public BaokaiHttpResult(){
    }
    public static <Result> BaokaiHttpResult<Result> of() {
        return new BaokaiHttpResult<>(0, HttpResultCodeEnum.SUCCESS.getMsg(), null);
    }

    public static <Result> BaokaiHttpResult<Result> of(Result data) {
        return new BaokaiHttpResult<>(0, HttpResultCodeEnum.SUCCESS.getMsg(), data);
    }

    public static <Result> BaokaiHttpResult<Result> of(HttpResultCodeEnum result,Result data) {
        return new BaokaiHttpResult<>(result.getCode(), result.getMsg(), data);
    }
    public static <Result> BaokaiHttpResult<Result> of(HttpResultCodeEnum result) {
        return new BaokaiHttpResult<>(result.getCode(), result.getMsg(), null);
    }

    public static <Result> BaokaiHttpResult<Result> of(int code) {
        return new BaokaiHttpResult<>(code, null, null);
    }

    public static <Result> BaokaiHttpResult<Result> of(int code, String msg) {
        return new BaokaiHttpResult<>(code, msg, null);
    }

    public static <Result> BaokaiHttpResult<Result> of(int code, String msg, Result data) {
        return new BaokaiHttpResult<>(code, msg, data);
    }


}
