package com.campuswall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.campuswall.**.mapper")
@SpringBootApplication
public class CampusWallApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusWallApplication.class, args);
    }
}
