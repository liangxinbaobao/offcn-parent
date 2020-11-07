package com.offcn.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: lhq
 * @Date: 2020/10/23 10:31
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.offcn.project.mapper")
public class ScwProjectStart {

    public static void main(String[] args) {
        SpringApplication.run(ScwProjectStart.class);
    }
}
