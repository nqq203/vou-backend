package com.vou.auth_service.service.imp;

import com.vou.auth_service.model.Admin;
import com.vou.auth_service.model.User;
import com.vou.auth_service.service.UserManagementClient;
import com.vou.auth_service.service.registration_interface.IRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminRegistration implements IRegistration {
    @Autowired
    private UserManagementClient client;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public boolean register(User user) {
        Optional<User> existingUserByUsername = client.getUserByUsername(user.getUsername());
        Optional<User> existingUserByEmail = client.getUserByEmail(user.getEmail());

        if (existingUserByUsername.isPresent()) {
            return false;
        }
        if (existingUserByEmail.isPresent()) {
            return false;
        }

        String password = passwordEncoder.encode(user.getPassword());
        Admin admin = new Admin(user, password);
        // Set other specific fields for Admin
        try {
            return client.createAdmin(admin);
        } catch (Exception e) {
            System.err.println("Failed to create admin: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifyOtp(String username, String otp) {
        return false;
    }

    @Override
    public String resendOtp(String email) {
        return null;
    }
}
