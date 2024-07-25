package com.vou.auth_service.service.imp;

import com.vou.auth_service.constant.Role;
import com.vou.auth_service.constant.Status;
import com.vou.auth_service.model.Admin;
import com.vou.auth_service.model.User;
import com.vou.auth_service.repository.AdminRepository;
import com.vou.auth_service.repository.UserRepository;
import com.vou.auth_service.service.registration_interface.IRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminRegistration implements IRegistration {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public boolean register(User user) {
        System.out.println(user);
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }
        String password = passwordEncoder.encode(user.getPassword());
        Admin admin = new Admin(user, password, Status.PENDING);
        // Set other specific fields for Admin
        adminRepository.save(admin);
        return true;
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
