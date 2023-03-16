package com.mybatisplus;


import love.forte.simboot.spring.autoconfigure.EnableSimbot;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;


@EnableSimbot
@SpringBootApplication
@MapperScan("com.mybatisplus.mapper")
@EnableCaching
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        FixProtocolVersion.update();
        SpringApplication.run(Application.class, args);
    }
}
