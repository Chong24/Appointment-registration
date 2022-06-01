package com.atguigu.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wang
 * @create 2022-05-29
 */
@Configuration
@MapperScan("com.atguigu.yygh.user.mapper")
public class UserConfig {
}
