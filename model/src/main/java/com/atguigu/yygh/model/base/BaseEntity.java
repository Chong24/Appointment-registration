package com.atguigu.yygh.model.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基本实体类，即每个表的公共内容
 * @author wang
 * @create 2022-05-12
 */
@Data
public class BaseEntity implements Serializable {

    //不加@JsonFormat，可能会导致返回给浏览器JSON数据时丢失
    @ApiModelProperty(value = "id")  //swagger提供的注解，value表示对属性做简单说明
    @TableId(type = IdType.AUTO)     //id的类型采用自增
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")   //json的格式
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除(1:已删除，0:未删除)")
    @TableLogic     //代表是逻辑删除
    @TableField("is_deleted")
    private Integer isDeleted;

    @ApiModelProperty(value = "其他参数")
    @TableField(exist = false)      ///代表对应的数据库的表可以不存在
    private Map<String,Object> param = new HashMap<>();
}