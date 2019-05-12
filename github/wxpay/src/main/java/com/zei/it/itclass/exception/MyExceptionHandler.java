package com.zei.it.itclass.exception;

import com.zei.it.itclass.domain.ResponseBean;
import com.zei.it.itclass.util.ResponseUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseBean Handler(Exception e){
        if(e instanceof MyException){
            return ResponseUtil.buildError(((MyException) e).getCode(),((MyException) e).getMsg());
        }
        return ResponseUtil.buildError("其他异常");
    }
}
