package com.atguigu.yygh.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wang
 * @create 2022-05-31
 */
@Configuration
@MapperScan("com.atguigu.yygh.order.mapper")
public class OrderConfig {
}
