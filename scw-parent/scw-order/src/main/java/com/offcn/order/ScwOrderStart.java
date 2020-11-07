package com.offcn.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: lhq
 * @Date: 2020/10/26 09:39
 * @Description:
 */





@SpringCloudApplication   //@SpringBootApplication+@EnableDiscoveryClient+@EnableCircuitBreaker
@EnableDiscoveryClient
@MapperScan("com.offcn.order.mapper")
@EnableFeignClients
@EnableCircuitBreaker
public class ScwOrderStart {

    public static void main(String[] args) {
        SpringApplication.run(ScwOrderStart.class);
    }
}
