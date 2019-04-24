package com.zjt.servicezjt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ServiceZjtApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceZjtApplication.class, args);
    }

}
