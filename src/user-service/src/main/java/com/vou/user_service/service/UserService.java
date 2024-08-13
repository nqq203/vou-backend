package com.vou.user_service.service;

import com.vou.user_service.common.NotFoundException;
import com.vou.user_service.constant.Regex;
import com.vou.user_service.constant.Role;
import com.vou.user_service.constant.Status;
import com.vou.user_service.model.*;
import com.vou.user_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private SessionRepository sessionRepository;

    public User updateUser(Long userId, Map<String, Object> updates) throws Exception{
        User updatedUser;
        try {
            updatedUser = userRepository.findById(userId).orElse(null);
        } catch (Exception e) {
            throw new Exception("Error updating user", e);
        }
        System.out.println("User updating: " + updates);
        if (updatedUser != null) {
            updates.forEach((key, value) -> {
                switch(key) {
                    case "username":
                        updatedUser.setUsername((String) value);
                        break;
                    case "password":
                        updatedUser.setPassword((String) value);
                        break;
                    case "fullName":
                        updatedUser.setFullName((String) value);
                        break;
                    case "email":
                        updatedUser.setEmail((String) value);
                        break;
                    case "phoneNumber":
                        updatedUser.setPhoneNumber((String) value);
                        break;
                    case "lockedDate":
                        updatedUser.setLockedDate((LocalDateTime) value);
                        break;
                    case "role":
                        Role role = checkRole((String) value);
                        updatedUser.setRole(role);
                        break;
                    case "status":
                        Status status = checkStatus((String) value);
                        updatedUser.setStatus(status);
                    default:
                        break;
                }
            });
        } else {
            throw new NotFoundException("User not found");
        }

        userRepository.save(updatedUser);
        return updatedUser;
    }

    private Role checkRole(String role) {
        if (role.equals("BRAND"))
            return Role.BRAND;
        else if (role.equals("ADMIN"))
            return Role.ADMIN;
        return Role.PLAYER;
    }

    private Status checkStatus(String status) {
        if (status.equals("PENDING"))
            return Status.PENDING;
        else if (status.equals("INACTIVE"))
            return Status.INACTIVE;
        return Status.ACTIVE;
    }

    public User createUser(User user) throws Exception {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new Exception("Error creating new user", e);
        }
    }

    public Admin createAdmin(Admin admin) throws Exception {
        try {
            return adminRepository.save(admin);
        } catch (Exception e) {
            throw new Exception("Error creating new admin");
        }
    }

    public Player createPlayer(Player player) throws Exception {
        try {
            return playerRepository.save(player);
        } catch (Exception e) {
            throw new Exception("Error creating new player");
        }
    }

    public Brand createBrand(Brand brand) throws Exception {
        try {
            return brandRepository.save(brand);
        } catch (Exception e) {
            throw new Exception("Error creating new brand");
        }
    }

    public User findByIdentifier(String identifier) throws Exception{
        Pattern emailPattern = Pattern.compile(Regex.EMAIL_REGEX);
        Matcher matcher = emailPattern.matcher(identifier);
        User userFound = null;
        try {
            if (matcher.matches()) {
                userFound = userRepository.findByEmail(identifier);
            } else {
                userFound = userRepository.findByUsername(identifier);
            }
        } catch (Exception e) {
            throw new Exception("Error finding user by email or username", e);
        }
        if (userFound != null) {
            return userFound;
        } else
            throw new NotFoundException("User not found");
    }

    public User findByIdUser(Long userId) throws Exception {
        User userFound;
        try {
            userFound = userRepository.findByIdUser(userId);

        } catch (Exception e) {
            throw new Exception("Error finding user by id", e);
        }
        if (userFound == null)
            throw new NotFoundException("User not found");
        return userFound;
    }

    public Player findPlayerByUserId(Long id) throws Exception {
        Player playerFound;
        try {
            playerFound = playerRepository.findByIdUser(id);
        } catch (Exception e) {
            throw new Exception("Error finding player");
        }
        if (playerFound == null)
            throw new NotFoundException("Player not found");
        return playerFound;
    }

    public Player updatePlayer(Player player) throws Exception {
        try {
            return playerRepository.save(player);
        } catch (Exception e) {
            throw new Exception("Error update player", e);
        }
    }

    public Session createSession(Session session) throws Exception {
        try {
            return sessionRepository.save(session);
        } catch (Exception e) {
            throw new Exception("Error creating new session", e);
        }
    }

    public Session findSessionByToken(String token) {
        return sessionRepository.findByIdSession(token).orElse(null);
    }

    public Session updateSession(Session session) {
        return sessionRepository.save(session);
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public User updateUserInternal(User user) {
        return userRepository.save(user);
    }

}
