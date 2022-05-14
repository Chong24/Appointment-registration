package com.atguigu.yygh.hosp.mapper;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 使用的是Mybatis-plus，它提供了一个基础的BaeMapper，里面定义了一些基础方法
 *
 * 实体类都放在了model模块下，所以在pom依赖中需要引入model依赖
 * @author wang
 * @create 2022-05-12
 */
public interface HospitalSetMapper extends BaseMapper<HospitalSet>{
}
