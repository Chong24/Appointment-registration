package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 前台医院控制端，即管理系统上传数据存到mongondb，然后显示在前台预约挂号平台
 * @author wang
 * @create 2022-05-28
 */
@Api(tags = "医院详情管理")
@RestController
@RequestMapping("admin/hosp/hospital")
//@CrossOrigin
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    //医院列表(条件查询分页)
    @ApiOperation("医院列表")
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo){  //将查询的条件参数封装到了HospitalSetQueryVo中
        //此时医院的数据存在于mongodb中
        Page<Hospital> pageModel = hospitalService.selectHospPage(page,limit,hospitalQueryVo);
        List<Hospital> content = pageModel.getContent();
        long totalElements = pageModel.getTotalElements();
        return Result.ok(pageModel);
    }

    //更新医院的上限状态
    @ApiOperation("更新医院的上限状态")
    @GetMapping("updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable("id") String id,
                                   @PathVariable("status") Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    //查询医院详情
    @ApiOperation("查询医院详情")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetail(@PathVariable("id") String id){
        //为了方便后续取值方便，此处查询不用Hospital接受，而是放在集合里面
        Map<String, Object> map = hospitalService.getHospById(id);
        return Result.ok(map);
    }

}
