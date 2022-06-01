package com.atguigu.yygh.vo.cmn;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 导入导出excel表的对应实体类
 * @author wang
 * @create 2022-05-23
 */
@Data
public class DictEeVo {

    //@ExcelProperty设置表头名称，index是位置，从0开始
    @ExcelProperty(value = "id" ,index = 0)
    private Long id;

    @ExcelProperty(value = "上级id" ,index = 1)
    private Long parentId;

    @ExcelProperty(value = "名称" ,index = 2)
    private String name;

    @ExcelProperty(value = "值" ,index = 3)
    private String value;

    @ExcelProperty(value = "编码" ,index = 4)
    private String dictCode;

}
