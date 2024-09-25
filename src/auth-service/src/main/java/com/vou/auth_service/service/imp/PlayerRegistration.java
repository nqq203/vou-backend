package com.vou.auth_service.service.imp;

import com.vou.auth_service.constant.Status;
import com.vou.auth_service.model.ItemRepo;
import com.vou.auth_service.model.Player;
import com.vou.auth_service.model.User;
import com.vou.auth_service.service.InventoryClient;
import com.vou.auth_service.service.OtpService;
import com.vou.auth_service.service.UserManagementClient;
import com.vou.auth_service.service.registration_interface.IRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerRegistration implements IRegistration {
    @Autowired
    private UserManagementClient client;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OtpService otpService;
    @Autowired
    private InventoryClient inventoryClient;

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
        System.out.println("Co user" + user.getIdUser());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        Player player = new Player(user, encodedPassword);
        try {
            Long idUser = client.createPlayer(player);
            System.out.println("Qua day 2");
            if (idUser != null) {
                String otp = otpService.generateOtp();
                otpService.storeOtp(player.getUsername(), otp);
                if (player.getEmail() != null) {
                    otpService.sendOtpEmail(player.getEmail(), otp);
                    System.out.println("Qua day 3");
                }
                List<ItemRepo> itemRepoList = inventoryClient.createItemRepo(idUser).orElse(null);
                if (itemRepoList == null) {
                    return 3;
                }
                for (ItemRepo itemRepo : itemRepoList) {
                    System.out.println(itemRepo);
                }
            }
            return 1;
        } catch (Exception e) {
            System.err.println("Failed to create player: " + e.getMessage());
            return 2;
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
        User user = client.getUserByIdentifier(username).orElse(null);
        if (user == null) {
            System.out.println("No user found with username: " + username);
            return false;
        }

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
        } else {
            otpService.sendOtpEmail(email, newOtp);
        }
        return newOtp;
    }
}
