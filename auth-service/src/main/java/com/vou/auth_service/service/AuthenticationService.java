package com.vou.auth_service.service;

import com.vou.auth_service.constant.Status;
import com.vou.auth_service.model.Player;
import com.vou.auth_service.model.Session;
import com.vou.auth_service.model.User;
import com.vou.auth_service.repository.PlayerRepository;
import com.vou.auth_service.repository.SessionRepository;
import com.vou.auth_service.repository.UserRepository;
import com.vou.auth_service.service.JwtService;
import com.vou.auth_service.service.imp.PlayerRegistration;
import com.vou.auth_service.service.registration_factory.RegistrationFactory;
import com.vou.auth_service.service.registration_interface.IRegistration;
import com.vou.auth_service.strategy.TokenStrategy;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RegistrationFactory registrationFactory;


    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null ) {
            System.out.println(user.getUsername());
        }
        else {
            return null;
        }
        Player player = playerRepository.findByIdUser(user.getIdUser());
        if (player == null) {
            return null;
        }
        if (player.getStatus() != Status.ACTIVE) {
            return "invalid";
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtService.generateToken(user);
            Date expirationDate = jwtService.getExpirationDateFromToken(token);
            Session session = new Session();
            session.setToken(token);
            session.setActive(true);
            session.setExpiration(expirationDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            sessionRepository.save(session);
            return token;
        }
        return null;
    }

    public boolean register(User user) {
//        System.out.println("789");
//        if (userRepository.existsByUsername(user.getUsername())) {
//            return false;
//        }
//        System.out.println("789");
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//        System.out.println("91011");
//        return true;
        IRegistration registrationService = registrationFactory.getRegistration(user.getRole().toString());
        return registrationService.register(user);
    }

    public boolean logout(String token) {
        System.out.println(token);
        if (jwtService.validateTokenAndGetUsername(token) != null) {
            Session session = sessionRepository.findByToken(token).orElse(null);
            if (session != null) {
                session.setActive(false);
                session.setLogoutAt(LocalDateTime.now());
                sessionRepository.save(session);
                return true;
            }
        }
        return false;
    }

    public boolean isTokenBlackListed(String token) {
        Session session = sessionRepository.findByToken(token).orElse(null);
        return session != null && !session.isActive();
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean verifyOtp(String username, String otp) {
        IRegistration registrationService = registrationFactory.getRegistration("player");
        return registrationService.verifyOtp(username, otp);
    }

    public String resendOtp(String email) {
        IRegistration registrationService = registrationFactory.getRegistration("player");
        return registrationService.resendOtp(email);
    }

    public boolean changePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);
            return true;
        }

        return false;
    }
}
