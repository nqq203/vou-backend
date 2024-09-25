package com.vou.auth_service.service.imp;

import com.vou.auth_service.model.Brand;
import com.vou.auth_service.model.User;
import com.vou.auth_service.service.UserManagementClient;
import com.vou.auth_service.service.registration_interface.IRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BrandRegistration implements IRegistration {
    @Autowired
    private UserManagementClient client;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public byte register(User user) {
        try {
            User existUser = client.getUserByUsernameAndEmail(user.getUsername(), user.getEmail()).orElse(null);
            if (existUser != null) {
                return 0;
            }
        } catch (Exception e) {
            return 2;
        }

        String password = passwordEncoder.encode(user.getPassword());
        Brand brand = new Brand(user, password);
        try {
            if (client.createBrand(brand)) {
                return 1;
            }
            return 2;
        } catch (Exception e) {
            System.err.println("Failed to create admin: " + e.getMessage());
            return 2;
        }
    }

    @Override
    public boolean verifyOtp(String username, String otp) {
        return false;
    }

    @Override
    public String resendOtp(String username, String email) {
        return null;
    }
}
