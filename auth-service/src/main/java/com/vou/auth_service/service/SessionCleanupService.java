package com.vou.auth_service.service;

import com.vou.auth_service.model.Session;
import com.vou.auth_service.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionCleanupService {

    @Autowired
    private SessionRepository sessionRepository;

    @Scheduled(fixedRate = 60000) // Chạy mỗi phút
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        List<Session> sessions = sessionRepository.findAll();
        for (Session session : sessions) {
            if (session.getExpiration().isBefore(now) && session.isActive()) {
                session.setActive(false);
                sessionRepository.save(session);
            }
        }
    }
}
