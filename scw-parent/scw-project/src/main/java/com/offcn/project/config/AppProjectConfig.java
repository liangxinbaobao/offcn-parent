package com.offcn.project.config;

import com.offcn.utils.OSSTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: lhq
 * @Date: 2020/10/23 10:17
 * @Description:
 */
@Configuration
public class AppProjectConfig {

    //加载配置文件中的oss属性
    @ConfigurationProperties(prefix = "oss")
    @Bean   //<bean id="" class="">
    public OSSTemplate ossTemplate(){
        return new OSSTemplate();
    }
}
