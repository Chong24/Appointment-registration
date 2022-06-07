package com.atguigu.hospital.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2配置信息
 * @author qy
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket webApiConfig(){

        //最终所有的配置信息都要存放在docket中
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")    //这个注解用于配置当前docket的名称，每一个docket都相当于一个组
                .apiInfo(webApiInfo())
                .select()           //设置哪些类的哪些方法能够添加到Swagger
                //过滤掉admin路径下的所有页面
                .paths(Predicates.and(PathSelectors.regex("/P2P/.*")))
                //过滤掉所有error或error.*页面
                //.paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();

    }

    /**
     * 接口详细信息：包括标题、描述、版本、联系人等
     * @return
     */
    private ApiInfo webApiInfo(){

        return new ApiInfoBuilder()
                .title("网站-API文档")
                .description("本文档描述了网站微服务接口定义")
                .version("1.0")
                .contact(new Contact("qy", "http://atguigu.com", "55317332@qq.com"))
                .build();
    }


}
