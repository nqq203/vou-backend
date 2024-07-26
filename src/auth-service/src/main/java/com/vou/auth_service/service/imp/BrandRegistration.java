package com.vou.auth_service.service.imp;

import com.vou.auth_service.constant.Role;
import com.vou.auth_service.constant.Status;
import com.vou.auth_service.model.Brand;
import com.vou.auth_service.model.User;
import com.vou.auth_service.repository.BrandRepository;
import com.vou.auth_service.repository.UserRepository;
import com.vou.auth_service.service.registration_interface.IRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BrandRegistration implements IRegistration {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public boolean register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }
        String password = passwordEncoder.encode(user.getPassword());
        Brand brand = new Brand(user, password, Status.PENDING);
        // Set other specific fields for Admin
        brandRepository.save(brand);
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
