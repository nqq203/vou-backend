package com.vou.auth_service.service.imp;

import com.vou.auth_service.constant.Role;
import com.vou.auth_service.constant.Status;
import com.vou.auth_service.model.Player;
import com.vou.auth_service.model.User;
import com.vou.auth_service.repository.PlayerRepository;
import com.vou.auth_service.repository.UserRepository;
import com.vou.auth_service.service.OtpService;
import com.vou.auth_service.service.registration_interface.IRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PlayerRegistration implements IRegistration {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private OtpService otpService;

    @Override
    public boolean register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }
        String password = passwordEncoder.encode(user.getPassword());
        Player nPlayer = new Player(user, password, Status.PENDING);
        // Set other specific fields for Player
        playerRepository.save(nPlayer);

        //Generate and send OTP
        String otp = otpService.generateOtp();
        otpService.storeOtp(nPlayer.getUsername(), otp);

        if (nPlayer.getEmail() != null) {
            otpService.sendOtpEmail(nPlayer.getEmail(), otp);
        }

        return true;
    }

    @Override
    public boolean verifyOtp(String username, String otp) {
        System.out.print("In Player Registration: " + username + " " + otp);
        if (otpService.validateOtp(username, otp)) {
            User user = userRepository.findByUsername(username);
            Player player = playerRepository.findByIdUser(user.getIdUser());
            System.out.println("In verify otp: " + user + " " + player);
            if (player != null) {
                player.setStatus(Status.ACTIVE);
                playerRepository.save(player);
                System.out.println("Player Registration: vao dung");
                return true;
            }
        }
        return false;
    }

    @Override
    public String resendOtp(String email) {
        return otpService.resendOtp(email);
    }
}
