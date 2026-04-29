package com.ecommerce.campusai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.ecommerce.campusai.mapper")
@EnableCaching
@EnableScheduling
public class CampusAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusAiApplication.class, args);
    }

}
