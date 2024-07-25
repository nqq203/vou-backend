package com.vou.auth_service.service.registration_factory;

import com.vou.auth_service.service.imp.AdminRegistration;
import com.vou.auth_service.service.imp.BrandRegistration;
import com.vou.auth_service.service.imp.PlayerRegistration;
import com.vou.auth_service.service.registration_interface.IRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationFactory {
    @Autowired
    private PlayerRegistration playerRegistration;
    @Autowired
    private AdminRegistration adminRegistration;
    @Autowired
    private BrandRegistration brandRegistration;

    public IRegistration getRegistration(String role) {
        switch (role.toLowerCase()) {
            case "player":
                return playerRegistration;
            case "admin":
                return adminRegistration;
            case "brand":
                return brandRegistration;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
