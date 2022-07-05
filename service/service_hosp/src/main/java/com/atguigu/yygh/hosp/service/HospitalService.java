package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 对Hospital表（存在Mongondb数据库中）的service层
 * @author wang
 * @create 2022-05-24
 */
public interface HospitalService {

    //上传医院接口
    void save(Map<String, Object> parapMap);

    //实现根据医院编号查询
    Hospital getByHoscode(String hoscode);

    //医院列表(条件查询分页)
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    //更新医院的上限状态
    void updateStatus(String id, Integer status);

    //查询医院详情
    Map<String, Object> getHospById(String id) throws ExecutionException, InterruptedException;

    //获取医院名称
    String getHospName(String hoscode);

    //根据医院名称查询
    List<Hospital> findByHosname(String hosname);

    //根据医院编号获取预约挂号详情
    Map<String, Object> item(String hoscode) throws ExecutionException, InterruptedException;

}
