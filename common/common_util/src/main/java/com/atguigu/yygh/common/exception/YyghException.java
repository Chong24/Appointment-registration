package com.atguigu.yygh.common.exception;

import com.atguigu.yygh.common.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自定义的全局异常类：extends RuntimeException,
 * RuntimeException是运行时异常，它RuntimeException extends Exception(分为运行时异常、非运行时异常)
 * Exception extends Throwable(子类Error, Exception );
 * Throwable implements Serializable
 * 只要系统在运行中出现了异常，就会被其捕捉：前提是，得把异常抛出来
 * @author wang
 * @create 2022-05-18
 */

@Data
@ApiModel(value = "自定义全局异常类")
public class YyghException extends RuntimeException{

    @ApiModelProperty(value = "异常状态码")
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param message
     * @param code
     */
    public YyghException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public YyghException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "YyghException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
