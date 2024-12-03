package com.bupt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.bupt.mapper")
@EnableScheduling
public class BaokaiWmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaokaiWmsApplication.class, args);
    }
}
