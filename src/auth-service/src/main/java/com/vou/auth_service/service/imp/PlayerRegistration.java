package com.vou.auth_service.service.imp;

import com.vou.auth_service.constant.Status;
import com.vou.auth_service.model.Player;
import com.vou.auth_service.model.User;
import com.vou.auth_service.service.OtpService;
import com.vou.auth_service.service.UserManagementClient;
import com.vou.auth_service.service.registration_interface.IRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        Optional<User> existingUserByUsername = client.getUserByIdentifier(user.getUsername());
        Optional<User> existingUserByEmail = client.getUserByIdentifier(user.getEmail());
        if (existingUserByUsername.isPresent()) {
            return false;
        }
        if (existingUserByEmail.isPresent()) {
            return false;
        }

        System.out.println("Qua day 1");
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        Player player = new Player(user, encodedPassword);
        // Set other specific fields for Admin

        try {
            boolean isSaved = client.createPlayer(player);
            System.out.println("Qua day 2");
            if (isSaved) {
                //Generate and send OTP
                String otp = otpService.generateOtp();
                otpService.storeOtp(player.getUsername(), otp);

                if (player.getEmail() != null) {
                    otpService.sendOtpEmail(player.getEmail(), otp);
                    System.out.println("Qua day 3");
                }
            }

            return isSaved;
        } catch (Exception e) {

            System.err.println("Failed to create player: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifyOtp(String username, String otp) {
        System.out.println("In Player Registration: " + username + " " + otp);
        // Validate the OTP first
        if (!otpService.validateOtp(username, otp)) {
            System.out.println("OTP validation failed for username: " + username);
            return false;
        }

        // Retrieve the user using the username
        Optional<User> optionalUser = client.getUserByIdentifier(username);
        if (!optionalUser.isPresent()) {
            System.out.println("No user found with username: " + username);
            return false;
        }

        User user = optionalUser.get();
        user.setStatus(Status.ACTIVE);
        return client.updateUserInternal(user) != null;
    }

    @Override
    public String resendOtp(String username, String email) {
        Optional<User> existingUserByUsername = client.getUserByIdentifier(username);
        User user = null;
        if (existingUserByUsername.isPresent()) {
            user = existingUserByUsername.get();
        }
        if (user == null) {
            return null;
        }

        String newOtp = otpService.generateOtp();
        otpService.storeOtp(username, newOtp);
        if (email == null) {
            otpService.sendOtpEmail(user.getEmail(), newOtp);
        }
        else {
            otpService.sendOtpEmail(email, newOtp);
        }
        return newOtp;
    }
}
