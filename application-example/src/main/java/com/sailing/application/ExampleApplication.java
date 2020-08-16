package com.sailing.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 统一启动入口
 */
@ComponentScan(value = {"com.sailing.*"})
@EnableFeignClients(value = {"com.sailing.*"})
@SpringBootApplication
@EnableAsync
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}

