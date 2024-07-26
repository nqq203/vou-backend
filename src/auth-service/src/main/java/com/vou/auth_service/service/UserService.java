package com.vou.auth_service.service;

import com.vou.auth_service.constant.Role;
import com.vou.auth_service.model.User;
import com.vou.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
                    default:
                        break;
                }
            });
            userRepository.save(updatedUser);
        }

        return updatedUser;
    }
}
