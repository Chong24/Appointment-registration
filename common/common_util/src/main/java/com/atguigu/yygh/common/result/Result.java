package com.atguigu.yygh.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 全局统一返回结果类：因为现在都是前后端分离开发项目，因此前后端都是用json来交互数据。
 * 一般结果类会有三个属性：code(状态码)、message(信息)，data(数据——泛型)
 * @author wang
 * @create 2022-05-18
 */
@Data
@ApiModel(value = "全局统一返回结果")
public class Result<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;

    //空参构造器
    public Result() {

    }

    //静态泛型方法，之所以用static，就是可以直接类.方法名调用。 提供三种重载的方法

    //工具方法，起到一个判断是否有数据的作用
    public static <T> Result<T> build(T data){
        Result<T> result = new Result<>();
        if(data != null){
            result.setData(data);
        }
        return result;
    }

    /**
     * 有返回数据的
     * @param data
     * @param resultCodeEnum
     * @param <T>
     * @return
     */
    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum){
        Result<T> result = build(data);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    /**
     * 自定义返回code、message，但没有返回数据
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> build(Integer code, String message){
        Result<T> result = build(null);
        result.setMessage(message);
        result.setCode(code);
        return result;
    }

    /**
     * 操作成功，没有返回数据的
     * @param <T>
     * @return
     */
    public static <T> Result<T> ok(){
        return Result.ok(null);
    }

    /**
     * 操作成功，且有返回数据的
     * @param data 数据
     * @param <T>   数据类型
     * @return  统一返回结果
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = build(data);
        return build(data, ResultCodeEnum.SUCCESS);
    }

    /**
     * 操作失败，没有返回数据的
     * @param <T>
     * @return
     */
    public static<T> Result<T> fail() {
        return Result.fail(null);
    }

    /**
     * 操作失败，有返回数据的
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(T data) {
        Result<T> result = build(data);
        return result.build(data, ResultCodeEnum.FAIL);
    }

    /**
     * 只有信息的返回对象
     * @param msg
     * @return
     */
    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    /**
     * 只有状态码的返回对象
     * @param code
     * @return
     */
    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    /**
     * 判断是否是成功的状态码
     * @return
     */
    public boolean isOk(){
        if(this.getCode().intValue() == ResultCodeEnum.SUCCESS.getCode().intValue()){
            return true;
        }
        return false;
    }
}
