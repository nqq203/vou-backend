/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 13/08/2024 - 23:08
 */
package com.vou.streaming_service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.TaskScheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class EventSchedulerService {
    @Autowired
    private TaskScheduler taskScheduler;

    public void scheduleJob(LocalDateTime eventDate, Runnable job) {
        Date date = Date.from(eventDate.atZone(ZoneId.systemDefault()).toInstant());
        taskScheduler.schedule(job, date);
    }
}
