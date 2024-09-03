package com.vou.event_service;

//import com.vou.event_service.config.S3Config;
import com.vou.event_service.config.S3Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(S3Config.class)
@EnableScheduling
public class EventServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EventServiceApplication.class, args);
	}
}
