package com.vou.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    private EmailService emailService;

    private static final long OTP_VALID_DURATION = 60; // 60 seconds

    private final Map<String, OtpEntry> otpStorage = new HashMap<>();

    private static class OtpEntry {
        private final String otp;
        private final LocalDateTime timestamp;

        public OtpEntry(String otp, LocalDateTime timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendOtpEmail(String email, String otp) {
        emailService.sendEmail(email, "Your OTP Code", "Your OTP code is: " + otp);
    }

    public void storeOtp(String key, String otp) {
        otpStorage.remove(key);
        OtpEntry otpEntry = new OtpEntry(otp, LocalDateTime.now());
        otpStorage.put(key, otpEntry);
    }

    public boolean validateOtp(String key, String otp) {
        OtpEntry otpEntry = otpStorage.get(key);
        if (otpEntry == null) {
            return false; // OTP not found
        }
        LocalDateTime currentTime = LocalDateTime.now();
        long secondsElapsed = java.time.Duration.between(otpEntry.getTimestamp(), currentTime).getSeconds();

        if (secondsElapsed > OTP_VALID_DURATION) {
            otpStorage.remove(key); // OTP expired, remove from storage
            return false; // OTP expired
        }

        return otp.equals(otpEntry.getOtp());
    }
}
