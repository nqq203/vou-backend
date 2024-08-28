package com.vou.reward_service;

//import com.vou.reward_service.config.S3Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
<<<<<<<< HEAD:src/reward-service/src/main/java/com/vou/reward_service/RewardServiceApplication.java
public class RewardServiceApplication {
========
//@EnableConfigurationProperties(S3Config.class)
public class InventoryAndRewardsService {
>>>>>>>> 6907e5439f9f435c74afe2e5aaca66b648bc9cf2:src/inventory-and-reward-service/src/main/java/com/vou/reward_service/InventoryAndRewardsService.java

    public static void main(String[] args) {
        SpringApplication.run(RewardServiceApplication.class, args);
    }
}
