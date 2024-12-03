package com.bupt.controller;

import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice(basePackages = "com.bupt.controller")
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public HttpResult<?> bindException(BindException e) {
        log.error("Spring通用检查参数校验不通过。BindException ->{}", e.getMessage());
        List<ObjectError> objectErrorList = e.getAllErrors();
        List<String> errorMessageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(objectErrorList)) {
            //错误非空
            for (ObjectError error : objectErrorList) {
                errorMessageList.add(error.getDefaultMessage());
            }
        }
        return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR, errorMessageList.get(0));
    }

    @ExceptionHandler(NullPointerException.class)
    public HttpResult<?> handleOtherException(NullPointerException e) {
        log.error("------------------空指针异常-------------------",e);
        log.error("--------------------end---------------------");
        return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR, "空指针问题--"+e);
    }

    @ExceptionHandler(Exception.class)
    public HttpResult<?> handleOtherException(Exception e) {
        log.error("------------------异常问题-------------------",e);
        log.error("--------------------end---------------------");
        return HttpResult.of(HttpResultCodeEnum.SYSTEM_ERROR);
    }
}

