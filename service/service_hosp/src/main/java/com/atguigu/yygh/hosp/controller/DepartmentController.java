package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端的科室的控制器
 * @author wang
 * @create 2022-05-27
 */
@Api(tags = "医院科室管理")
@RestController
@RequestMapping("/admin/hosp/department")
//@CrossOrigin
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    //根据医院编号，查询所有科室列表
    @ApiOperation("查询所有科室列表")
    @GetMapping("/getDepList/{hoscode}")
    public Result getDepList(@PathVariable("hoscode") String hoscode){

        //大科室下面还有小科室，不止一个科室，所以将返回数据封装在DepartmentVo
        List<DepartmentVo> list = departmentService.findDepTree(hoscode);
        return Result.ok(list);
    }
}
