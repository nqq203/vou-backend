package com.vou.auth_service.controller;

import com.vou.auth_service.common.BadRequest;
import com.vou.auth_service.common.CreatedResponse;
import com.vou.auth_service.common.ErrorResponse;
import com.vou.auth_service.model.LoginRequest;
import com.vou.auth_service.model.RegisterRequest;
import com.vou.auth_service.common.SuccessResponse;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authenticationService.login(loginRequest.getUsername(), loginRequest.getPassword());
        System.out.println("Hello login");
        if (token != null) {
            LoginResponse loginResponse = new LoginResponse(token);
            return ResponseEntity.ok(new SuccessResponse("Login successfully", HttpStatus.OK, loginResponse));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse(
                        "Invalid username or password!",
                        HttpStatus.UNAUTHORIZED, null));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        System.out.println("Attempting to login user: " + registerRequest.getUsername());

        User newUser = new User(registerRequest.getUsername(),
                                registerRequest.getPassword(),
                                registerRequest.getFullName(),
                                registerRequest.getEmail(),
                                registerRequest.getPhoneNumber(),
                                Role.PLAYER);

        boolean result = authenticationService.register(newUser);
        if (result) {
            RegisterResponse registerResponse = new RegisterResponse("User registered successfully");
            return ResponseEntity.ok(new CreatedResponse(registerResponse));
        } else {
            return ResponseEntity.badRequest().body(new BadRequest("Username already exists"));
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        boolean result = authenticationService.logout(token);
        System.out.println("logut boolean:" + result);
        if (result) {
            System.out.println("Vao result");
            LogoutResponse logoutResponse = new LogoutResponse("");
            return ResponseEntity.ok(new SuccessResponse("Logout successfully", HttpStatus.OK, logoutResponse));
        } else {
            System.out.println("Khong vao result");
            LogoutResponse logoutResponse = new LogoutResponse();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid token", HttpStatus.UNAUTHORIZED, logoutResponse));
        }
    }
}