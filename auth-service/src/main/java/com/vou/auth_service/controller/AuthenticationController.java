package com.vou.auth_service.controller;

import com.vou.auth_service.constant.Role;
import com.vou.auth_service.model.*;
import com.vou.auth_service.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authenticationService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (token != null) {
            return ResponseEntity.ok(new LoginResponse(token, "Login successfully"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, "Invalid username or password"));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        System.out.println("Attempting to login user: " + registerRequest.getUsername());

        User newUser = new User(registerRequest.getUsername(),
                                registerRequest.getPassword(),
                                registerRequest.getFullName(),
                                registerRequest.getEmail(),
                                registerRequest.getPhoneNumber(),
                                Role.PLAYER);

        boolean result = authenticationService.register(newUser);
        if (result) {
            return ResponseEntity.ok(new RegisterResponse(true, "User registered successfully"));
        } else {
            return ResponseEntity.badRequest().body(new RegisterResponse(false, "Username already exists"));
        }
    }
}