package com.offcn.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.PathSelectors;
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
                .title("七易众筹项目说明文档")
                .description("七易众筹项目说明文档")
                .contact("刘老师")
                .version("1.0")
                .termsOfServiceUrl("http://www.ujiuye.com")
                .build();
    }

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.offcn.user.controller"))
                .paths(PathSelectors.any())
                .build();
    }


}
