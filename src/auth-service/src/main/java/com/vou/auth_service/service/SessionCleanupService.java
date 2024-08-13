package com.vou.auth_service.service;

import com.vou.auth_service.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SessionCleanupService {
    @Autowired
    private UserManagementClient client;

    @Scheduled(fixedRate = 60000) // Chạy mỗi phút
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        Optional<?> response = client.getListSession();
        if (!response.isPresent()) {
            return;
        }
        Optional<List<Session>> optionalSessions = client.getListSession();
        if (!optionalSessions.isPresent()) {
            System.out.println("No sessions retrieved or error in fetching sessions.");
            return;
        }

        List<Session> sessions = optionalSessions.get();
        for (Session session : sessions) {
            if (session.getExpirationTime().isBefore(now) && session.isActive()) {
                session.setActive(false);
                client.updateSession(session);
            }
        }
    }
}
