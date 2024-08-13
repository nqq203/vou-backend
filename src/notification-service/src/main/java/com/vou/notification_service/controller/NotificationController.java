package com.vou.notification_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @GetMapping("/status")
    public String getNotificationStatus() {
        return "Notification service is running";
    }
}