package com.atguigu.yygh.msm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wang
 * @create 2022-05-29
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源自动配置，如果不需要数据库的时候
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.atguigu")
public class ServiceMsmApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceMsmApplication.class, args);
    }
}
