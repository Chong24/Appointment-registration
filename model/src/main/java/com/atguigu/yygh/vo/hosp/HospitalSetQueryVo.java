package com.atguigu.yygh.vo.hosp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 封装查询HospitalSet的对象，即根据什么属性查询
 * @author wang
 * @create 2022-05-18
 */

@Data
public class HospitalSetQueryVo {

    @ApiModelProperty(value = "医院名称")
    private String hosname;

    @ApiModelProperty(value = "医院编号")
    private String hoscode;
}
