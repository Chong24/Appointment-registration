package com.atguigu.yygh.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 封装数据库的api路径和签名密钥，用于跟不同医院对接（验证这个医院是否是安全对接）
 * @author wang
 * @create 2022-05-18
 */
@Data
@ApiModel(description = "签名信息")
public class SignInfoVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "api基础路径")
    private String apiUrl;

    @ApiModelProperty(value = "签名秘钥")
    private String signKey;

}
