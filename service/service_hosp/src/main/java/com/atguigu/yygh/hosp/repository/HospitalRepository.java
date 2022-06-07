package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data提供的一个简单的对Mongodb非关系型数据库的CRUD操作
 * @author wang
 * @create 2022-05-24
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {

    //判断是否存在数据
    Hospital getHospitalByHoscode(String hoscode);
    //MongoRepository有一套规范，直接按照规范命名，会直接调用写好的方法(既有默认实现类)

    //根据医院名称查询
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
