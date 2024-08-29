package com.vou.auth_service.service;

import com.vou.auth_service.constant.Status;
import com.vou.auth_service.model.Session;
import com.vou.auth_service.model.User;
import com.vou.auth_service.service.registration_factory.RegistrationFactory;
import com.vou.auth_service.service.registration_interface.IRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    UserManagementClient client;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RegistrationFactory registrationFactory;
    @Autowired
    private OtpService otpService;


    public String login(String username, String password) {
        Optional<User> response = client.getUserByUsername(username);

        if (!response.isPresent()) {
            return null;
        }
        User user = response.get();

        if (user.getStatus() != Status.ACTIVE) {
            otpService.resendOtp(user.getEmail());
            return "invalid";
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtService.generateToken(user);
            Date expirationDate = jwtService.getExpirationDateFromToken(token);
            Session session = new Session();
            session.setToken(token);
            session.setActive(true);
            session.setExpiration(expirationDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            Session savedSession = client.createSession(session);
            if (savedSession == null) {
                return null;
            }
            return token;
        }
        return null;
    }

    // After verified otp, we will use this function to log in
    public String loginWithoutPassword(String username) {
        Optional<User> response = client.getUserByUsername(username);

        if (!response.isPresent()) {
            return null;
        }
        User user = response.get();

        String token = jwtService.generateToken(user);
        Date expirationDate = jwtService.getExpirationDateFromToken(token);
        Session session = new Session();
        session.setToken(token);
        session.setActive(true);
        session.setExpiration(expirationDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        Session savedSession = client.createSession(session);
        if (savedSession == null) {
            return null;
        }

        return token;
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
            Optional<Session> sessionRes = client.getSessionByToken(token);
            if (!sessionRes.isPresent()) {
                return false;
            }
            Session session = sessionRes.get();
            session.setActive(false);
            session.setLogoutAt(LocalDateTime.now());
            Session updatedSession = client.updateSession(session);
            if (updatedSession == null) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isTokenBlackListed(String token) {
        Optional<Session> sessionRes = client.getSessionByToken(token);
        if (!sessionRes.isPresent()) {
            return false;
        }
        else {
            Session session = sessionRes.get();
            return !session.isActive();
        }
    }

    public User loadUserByUsername(String username) {
        Optional<User> userRes = client.getUserByUsername(username);
        return userRes.get();
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
        Optional<User> userRes = client.getUserByEmail(email);
        if (!userRes.isPresent()) {
            return false;
        }

        User user = userRes.get();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        client.updateUserInternal(user);
        return true;
    }
}
