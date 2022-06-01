package com.atguigu.yygh.msm.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.msm.service.MsmService;
import com.atguigu.yygh.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author wang
 * @create 2022-05-29
 */
@RestController
@RequestMapping("/api/msm")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/send/{phone}")
    public Result sendCode(@PathVariable String phone){

        //1、从redis获取验证码，如果获取到，返回ok，key为手机号，value为验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)){
            return Result.ok();
        }

        //2、如果从redis获取不到，生成验证码(并不是由阿里云生成，而是我们自己生成传到阿里云)，整合短信服务进行发送
        code = RandomUtil.getSixBitRandom();   //生成6位的验证码
        //整合短信服务发送
        boolean isSend  = msmService.send(phone,code);
        if (isSend){
            //3、如果发送了，存在redis中，设置有效时间
            redisTemplate.opsForValue().set(phone,code,2, TimeUnit.MINUTES);
            return Result.ok();
        }else{
            return Result.fail().message("发送短信失败");
        }
    }

}

