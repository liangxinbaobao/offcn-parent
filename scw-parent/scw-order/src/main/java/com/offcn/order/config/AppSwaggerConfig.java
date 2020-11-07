package com.offcn.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 11:27
 * @Description:   swagger配置类
 */
@Configuration
@EnableSwagger2 //开启swagger
public class AppSwaggerConfig {


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("七易众筹项目说明文档（订单模块）")
                .description("给前端人员使用的说明接口文档")
                .contact("lhq")
                .version("1.0")
                .termsOfServiceUrl("http://www.ujiuye.com")
                .build();
    }

    @Bean("订单模块")
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.offcn.order.controller"))
                .paths(PathSelectors.any())
                .build();
    }


}
