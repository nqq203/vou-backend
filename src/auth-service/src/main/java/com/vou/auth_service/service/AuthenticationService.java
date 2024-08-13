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

    public String login(String username, String password) {
        Optional<User> response = client.getUserByIdentifier(username);

        if (!response.isPresent()) {
            return null;
        }
        User user = response.get();

        if (user.getStatus() != Status.ACTIVE) {
            resendOtp(user.getUsername(), user.getEmail());
            return "invalid";
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtService.generateToken(user);
            Date expirationDate = jwtService.getExpirationDateFromToken(token);
            Session session = new Session();
            session.setIdSession(token);
            session.setIdUser(user.getIdUser());
            session.setActive(true);
            session.setExpirationTime(expirationDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
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
        Optional<User> response = client.getUserByIdentifier(username);

        if (!response.isPresent()) {
            return null;
        }
        User user = response.get();

        String token = jwtService.generateToken(user);
        Date expirationDate = jwtService.getExpirationDateFromToken(token);
        Session session = new Session();
        session.setIdSession(token);
        session.setIdUser(user.getIdUser());
        session.setActive(true);
        session.setExpirationTime(expirationDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
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

    public boolean logout(String token, Long idUser) {
        System.out.println(token);
        System.out.println(jwtService.validateTokenAndGetUsername(token));
        if (jwtService.validateTokenAndGetUsername(token) != null) {
            Session session = client.getSessionByToken(token);
            System.out.println("valide token");
            System.out.println(session);
            if (session == null) {
                return false;
            }
            if (!session.getIdUser().toString().equals(idUser.toString())) {
                return false;
            }
            session.setActive(false);
            session.setLogoutAt(LocalDateTime.now());
            Session updatedSession = client.updateSession(session);
            return updatedSession != null;
        }
        return false;
    }

    public boolean isTokenBlackListed(String token) {
        Session session = client.getSessionByToken(token);
        if (session == null) {
            System.out.println("token false");
            return false;
        }
        System.out.println("token true");
        return !session.isActive();
    }

    public User loadUserByUsername(String username) {
        return client.getUserByIdentifier(username).orElse(null);
    }

    public boolean verifyOtp(String username, String otp) {
        IRegistration registrationService = registrationFactory.getRegistration("player");
        return registrationService.verifyOtp(username, otp);
    }

    public String resendOtp(String username, String email) {
        IRegistration registrationService = registrationFactory.getRegistration("player");
        return registrationService.resendOtp(username, email);
    }

    public boolean changePassword(String email, String newPassword) {
        Optional<User> userRes = client.getUserByIdentifier(email);
        if (!userRes.isPresent()) {
            return false;
        }

        User user = userRes.get();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        User updatedUser = client.updateUserInternal(user);
        return updatedUser != null;
    }

    public boolean validateToken(String token) {
        Session session = client.getSessionByToken(token);
        System.out.println("vao validate token o jwt service 2");
        boolean isValidToken = jwtService.validateToken(token);
        if (session != null && isValidToken) {
            System.out.println(session.getIdSession());
            return session.isActive();
        }
        return false;
    }
}
