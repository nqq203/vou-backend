package com.vou.reward_service;

//import com.vou.reward_service.config.S3Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableConfigurationProperties(S3Config.class)
public class InventoryAndRewardsService {

    public static void main(String[] args) {
        SpringApplication.run(InventoryAndRewardsService.class, args);
    }
}
