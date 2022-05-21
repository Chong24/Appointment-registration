package com.atguigu.yygh.common.exception;

import com.atguigu.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理：@ControlAdvice + @ExceptionHandler
 * @author wang
 * @create 2022-05-18
 */
@ControllerAdvice
@ResponseBody  //将统一异常返回的对象用json输出
public class GlobalExceptionHandler {
    //如果抛出一个异常，有更精准匹配它类型的，就会被那个处理，没有精准匹配的，就会被大范围的handler处理

    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(YyghException.class)
    public Result error(YyghException e){
        e.printStackTrace();
        return Result.fail();
    }
}
