package com.vou.user_service.service;

import com.vou.user_service.constant.Role;
import com.vou.user_service.constant.Status;
import com.vou.user_service.model.*;
import com.vou.user_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    public User updateUser(Long userId, Map<String, Object> updates) {
        User updatedUser = userRepository.findById(userId).orElse(null);
        if (updatedUser != null) {
            updates.forEach((key, value) -> {
                switch(key) {
                    case "idUser":
                        updatedUser.setIdUser((Long) value);
                        break;
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
                        updatedUser.setRole((Role) value);
                        break;
                    case "status":
                        updatedUser.setStatus((Status) value);
                    default:
                        break;
                }
            });
        }

        userRepository.save(updatedUser);
        return updatedUser;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findByIdUser(Long userId){
        return userRepository.findByIdUser(userId);
    }

    public Player findPlayerByUserId(Long id) {
        return playerRepository.findByIdUser(id);
    }

    public Player updatePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public Session findSessionByToken(String token) {
        return sessionRepository.findByToken(token).orElse(null);
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
