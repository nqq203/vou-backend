package com.vou.event_service;

import com.vou.event_service.config.S3Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(S3Config.class)
public class EventServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EventServiceApplication.class, args);
	}
}
