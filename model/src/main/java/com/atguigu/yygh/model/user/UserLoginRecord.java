package com.atguigu.yygh.model.user;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录记录
 * @author wang
 * @create 2022-05-18
 */
@Data
@ApiModel(description = "用户登录日志")
@TableName("user_login_record")
public class UserLoginRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "ip")
    @TableField("ip")
    private String ip;

}