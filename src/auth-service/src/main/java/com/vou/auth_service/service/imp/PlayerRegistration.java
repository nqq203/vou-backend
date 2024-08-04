package com.vou.auth_service.service.imp;

import com.vou.auth_service.constant.Status;
import com.vou.auth_service.model.Admin;
import com.vou.auth_service.model.Player;
import com.vou.auth_service.model.Session;
import com.vou.auth_service.model.User;
import com.vou.auth_service.service.AuthenticationService;
import com.vou.auth_service.service.JwtService;
import com.vou.auth_service.service.OtpService;
import com.vou.auth_service.service.UserManagementClient;
import com.vou.auth_service.service.registration_interface.IRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PlayerRegistration implements IRegistration {
    @Autowired
    private UserManagementClient client;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OtpService otpService;

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

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        Player player = new Player(user, encodedPassword);
        // Set other specific fields for Admin

        try {
            boolean isSaved = client.createPlayer(player);

            //Generate and send OTP
            String otp = otpService.generateOtp();
            otpService.storeOtp(player.getUsername(), otp);

            if (player.getEmail() != null) {
                otpService.sendOtpEmail(player.getEmail(), otp);
            }

//            return savedPlayer != null && savedPlayer.getIdUser() != null;
            return isSaved;
        } catch (Exception e) {

            System.err.println("Failed to create admin: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifyOtp(String username, String otp) {
        System.out.print("In Player Registration: " + username + " " + otp);
        // Validate the OTP first
        if (!otpService.validateOtp(username, otp)) {
            System.out.println("OTP validation failed for username: " + username);
            return false;
        }

        // Retrieve the user using the username
        Optional<User> optionalUser = client.getUserByUsername(username);
        if (!optionalUser.isPresent()) {
            System.out.println("No user found with username: " + username);
            return false;
        }

        User user = optionalUser.get();
        user.setStatus(Status.ACTIVE);
        return client.updateUserInternal(user);
    }

    @Override
    public String resendOtp(String email) {
        return otpService.resendOtp(email);
    }
}
