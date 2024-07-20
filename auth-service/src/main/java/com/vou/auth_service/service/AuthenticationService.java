package com.vou.auth_service.service;

import com.vou.auth_service.model.User;
import com.vou.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    private Set<String> blacklistedTokens = new HashSet<>();

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null ) {
            System.out.println(user.getUsername());
        }
        else {
            System.out.println("user null");
        }
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtService.generateToken(user);
        }
        return null;
    }

    public boolean register(User user) {
        System.out.println("789");
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }
        System.out.println("789");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        System.out.println("91011");
        return true;
    }

    public boolean logout(String token) {
        System.out.println(token);
        if (jwtService.validateTokenAndGetUsername(token) != null) {
            blacklistedTokens.add(token);
            System.out.println("blacklist token: " + blacklistedTokens);
            return true;
        }
        return false;
    }

    public boolean isTokenBlackListed(String token) {
        return blacklistedTokens.contains(token);
    }
}
