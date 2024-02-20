package com.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = { "com.post", "com.common" }
)
@EntityScan(basePackages = {"com.common.entity"})
@EnableFeignClients // feign
public class PostApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostApplication.class, args);
    }

}
