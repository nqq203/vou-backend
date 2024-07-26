package com.vou.auth_service.strategy;

import com.vou.auth_service.model.User;

public interface TokenStrategy {
    String generateToken(User user);
    boolean validateToken(String token);
}
