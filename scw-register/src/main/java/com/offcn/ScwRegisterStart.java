package com.offcn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Auther: lhq
 * @Date: 2020/10/22 09:49
 * @Description:
 */
@SpringBootApplication
@EnableEurekaServer
public class ScwRegisterStart {
    public static void main(String[] args) {
        SpringApplication.run(ScwRegisterStart.class);
    }
}
