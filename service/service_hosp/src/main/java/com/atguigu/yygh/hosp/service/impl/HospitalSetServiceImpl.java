package com.atguigu.yygh.hosp.service.impl;

import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * mybatis提供了基础的service实现类，
 * ServiceImpl<HospitalSetMapper, HospitalSet>第一个泛型是我们的xml接口（即要继承BaseMapper的），第二个参数是JavaBean
 * @author wang
 * @create 2022-05-12
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
}
