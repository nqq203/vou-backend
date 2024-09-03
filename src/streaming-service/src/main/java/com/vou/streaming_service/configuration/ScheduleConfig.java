/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 13/08/2024 - 23:29
 */
package com.vou.streaming_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ScheduleConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("TaskScheduler");
        scheduler.setRemoveOnCancelPolicy(true);
        return scheduler;
    }
}