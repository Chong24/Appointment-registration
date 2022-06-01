package com.atguigu.yygh.user.client;

import com.atguigu.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 告诉服务名和url即可
 * @author wang
 * @create 2022-05-31
 */
@FeignClient(value = "service-user")
@Repository
public interface PatientFeignClient {
    //根据id获取就诊人信息
    @GetMapping("/api/user/patient/inner/get/{id}")
    public Patient getPatientOrder(@PathVariable("id") Long id);

}