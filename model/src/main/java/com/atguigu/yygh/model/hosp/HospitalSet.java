package com.atguigu.yygh.model.hosp;

import com.atguigu.yygh.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 医院的实体类，对应表
 * @author wang
 * @create 2022-05-12
 */
@Data
@ApiModel(description = "医院设置")     //提供详细的类描述，swagger提供的
@TableName("hospital_set")
public class HospitalSet extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JsonFormat
    @ApiModelProperty(value = "医院名称")
    @TableField("hosname")
    private String hosname;

    @JsonFormat
    @ApiModelProperty(value = "医院编号")
    @TableField("hoscode")
    private String hoscode;

    @JsonFormat
    @ApiModelProperty(value = "api基础路径")
    @TableField("api_url")
    private String apiUrl;

    @JsonFormat
    @ApiModelProperty(value = "签名秘钥")
    @TableField("sign_key")
    private String signKey;

    @JsonFormat
    @ApiModelProperty(value = "联系人姓名")
    @TableField("contacts_name")
    private String contactsName;

    @JsonFormat
    @ApiModelProperty(value = "联系人手机")
    @TableField("contacts_phone")
    private String contactsPhone;

    @JsonFormat
    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Integer status;

}