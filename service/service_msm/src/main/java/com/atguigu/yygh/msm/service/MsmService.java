package com.atguigu.yygh.msm.service;

import com.atguigu.yygh.vo.msm.MsmVo;

/**
 * @author wang
 * @create 2022-05-29
 */
public interface MsmService {
    boolean send(String phone, String code);

    //使用mq发送短信
    boolean send(MsmVo msmVo);
}
