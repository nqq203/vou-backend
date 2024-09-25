package com.vou.auth_service.service.registration_interface;

import com.vou.auth_service.model.User;

public interface IRegistration {
    byte register(User user);
    boolean verifyOtp(String username, String otp);
    String resendOtp(String username, String email);
}
