package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * @author wang
 * @create 2022-05-12
 */
@Api(tags = "医院设置管理")  //swagger页面显示信息
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
//@CrossOrigin  //代表允许跨域访问：所谓的跨域就是ip、端口、协议三者至少一个不同
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 查询医院设置表里的所有信息
     * 返回Result，因为Result对结果进行了统一的封装，可以显示不同的信息
     *
     * @return
     */
    @ApiOperation(value = "获取所有的医院设置信息")  //swagger页面显示信息
    @GetMapping("/findAll")
    public Result findAllHospital() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    /**
     * 逻辑删除医院设置的一条信息：要想逻辑删除，用JavaBean的哪个属性表示逻辑删除，就需要在这个属性字段上标注解@TableLogic
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id逻辑删除医院")
    @DeleteMapping("/{id}")
    public Result removeHospSet(@PathVariable long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 条件查询带分页：一般分页需要当前页，每页能显示几条；@RequestBody  接收前端传递给后端的json字符串中的数据，用于post请求
     * 前提：需要mybatis-plus的分页插件
     * @param current
     * @param limit
     * @param hospitalSetQueryVo 传入这个参数，是为了以后如果有搜索筛选的分页时，hospitalSetQueryVo封装了查询需要的筛选条件
     * @return
     */
    @ApiOperation(value = "分页查询")
    @PostMapping("/findPage/{current}/{limit}")
    public Result findPageHospSet(@PathVariable Long current,
                                  @PathVariable Long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {

        //创建page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);

        //构造查询条件
        String hosname = hospitalSetQueryVo.getHosname();  //医院名称
        String hoscode = hospitalSetQueryVo.getHoscode();   //医院编号
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();

        //如果传入了查询参数，就更新查询条件
        if (!StringUtils.isEmpty(hosname)) {
            //这里用的是模糊查询，因为有时候查医院不会打全名，打关键字即可，比如协和
            wrapper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode", hoscode);
        }

        //按条件查询分页，Page中封装了查询出的数据和分页的信息（例如当前页等）
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);
        //返回结果
        return Result.ok(hospitalSetPage);
    }

    /**
     * 添加医院：@RequestBody来接受前端表单传来的JSON数据，只有post方法有请求体
     *
     * @param hospitalSet
     * @return
     */
    @ApiOperation(value = "添加医院")
    @PostMapping("/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //由于医院可能有不可用的时候，比如下班了，所以我们需要一个数据库字段status来表示医院是否还可营业
        //设置状态 1：可以使用 0：不可使用
        hospitalSet.setStatus(1);

        //签名秘钥：用MD5对时间戳+随机数进行加密作为密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));

        //医院的其他信息，都通过前端的表单注入到hospitalSet对象里了
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 根据id获取医院设置
     * @param id
     * @returnl
     */
    @ApiOperation(value = "根据id获取医院设置")
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);

        return Result.ok(hospitalSet);
    }

    /**
     * 修改医院设置
     * @param hospitalSet
     * @return
     */
    @ApiOperation(value = "修改医院设置")
    @PostMapping("/updateHospSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet) {
        //会根据传入的hospitalSet对象的id，进行查找，然后将hospitalSet的值赋给它：先查后改
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 批量删除医院设置接口
     * @param idList
     * @return
     */
    @ApiOperation(value = "批量删除医院设置接口")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean flag = hospitalSetService.removeByIds(idList);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 医院设置锁定和解锁
     * @param id
     * @param status
     * @return
     */
    @ApiOperation(value = "医院设置锁定和解锁")
    @PutMapping("/lockHospSet/{id}/{status}")
    public Result lockHospSet(@PathVariable long id,
                              @PathVariable Integer status) {
        //根据id查询设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 发送签名的秘钥KEY
     * @param id
     * @return
     */
    @ApiOperation(value = "发送签名的秘钥KEY")
    @PutMapping("/sendKey/{id}")
    public Result lockHospSet(@PathVariable long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();

        //发送短信
        return Result.ok();
    }
}
