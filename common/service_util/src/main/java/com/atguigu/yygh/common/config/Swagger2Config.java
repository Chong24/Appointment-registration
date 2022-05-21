package com.atguigu.yygh.common.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2：Swagger2可以快速帮助我们编写最新的API接口文档
 * swagger通过注解表明该接口会生成文档，包括接口名、请求方法、参数、返回信息的等等。并且还可以直接测试执行，不需要再去postman测试
 * @Api：修饰整个类，描述Controller的作用，通常标在控制层(表现层)的类上
 * @ApiOperation：描述一个类的一个方法，或者说一个接口，通常标在控制器方法上
 * @ApiParam：单个参数描述，通常标在参数上
 * @ApiModel：用对象来接收参数，通常标在类上
 * @ApiModelProperty：用对象接收参数时，描述对象的一个字段，通常标在属性上
 * @ApiImplicitParam：一个请求参数
 * @ApiImplicitParams：多个请求参数
 *
 * http://localhost:8201/swagger-ui.html#/  即可以访问swagger自动生成的接口API文档。 8201是对应服务的端口号
 * @author wang
 * @create 2022-05-18
 */

/**
 * Swagger2的配置类：固定写法，只是里面的参数可以根据实际情况改变
 */
@Configuration    //配置类
@EnableSwagger2   //开启swagger2
public class Swagger2Config {

    @Bean
    public Docket webApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                //组名
                .groupName("webApi")
                //接口文档所包含的信息，
                .apiInfo(webApiInfo())
                //通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
                .select()
                //只显示api路径下的页面，即显示当前项目下的路径在swagger的接口文档上
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();

    }

    @Bean
    public Docket adminApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();

    }

    private ApiInfo webApiInfo(){

        return new ApiInfoBuilder()
                .title("网站-API文档")
                .description("本文档描述了网站微服务接口定义")
                .version("1.0")
                .contact(new Contact("wangchao", "http://www.baidu.com", "493211102@qq.com"))
                .build();
    }

    private ApiInfo adminApiInfo(){

        return new ApiInfoBuilder()
                .title("后台管理系统-API文档")
                .description("本文档描述了后台管理系统微服务接口定义")
                .version("1.0")
                .contact(new Contact("wangchao", "http://www.baidu.com", "49321112@qq.com"))
                .build();
    }
}
