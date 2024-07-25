package com.vou.auth_service.controller;

import com.vou.auth_service.common.*;
import com.vou.auth_service.entity.ChangePasswordRequest;
import com.vou.auth_service.entity.LoginRequest;
import com.vou.auth_service.entity.LoginResponse;
import com.vou.auth_service.entity.RegisterRequest;
import com.vou.auth_service.model.*;
import com.vou.auth_service.service.AuthenticationService;
import jakarta.validation.Valid;
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
        System.out.println("In login controller");
        if (token != null && !token.equals("invalid")) {
            LoginResponse loginResponse = new LoginResponse(token);
            return ResponseEntity.ok(
                    new SuccessResponse(
                            "Login successfully",
                            HttpStatus.OK, loginResponse));
        }
        if (token != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse("Invalid token",
                            HttpStatus.UNAUTHORIZED, null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse(
                        "Invalid username or password!",
                        HttpStatus.UNAUTHORIZED, null));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        System.out.println("Attempting to login user: " + registerRequest.getUsername());

        User newUser = new User(registerRequest.getUsername(),
                                registerRequest.getPassword(),
                                registerRequest.getFullName(),
                                registerRequest.getEmail(),
                                registerRequest.getPhoneNumber(),
                                registerRequest.getRole());

        boolean result = authenticationService.register(newUser);
        if (result) {
            return ResponseEntity.ok(new SuccessResponse(
                    "User registered successfully",
                    HttpStatus.CREATED, null));
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
            return ResponseEntity.ok(new SuccessResponse("Logout successfully", HttpStatus.OK, null));
        } else {
            System.out.println("Khong vao result");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid token", HttpStatus.UNAUTHORIZED, null));
        }
    }

    @PostMapping("/verify-otp")
//    public ResponseEntity<?> verifyOtp(@RequestParam String username, @RequestParam String otp) {
    public ResponseEntity<?> verifyOtp(@RequestParam String username, @RequestParam String otp) {
        System.out.println("vao verity-otp");
        boolean isVerified = authenticationService.verifyOtp(username, otp);
        System.out.println(isVerified);
        if (isVerified) {
            return ResponseEntity.ok(new SuccessResponse("OTP verified successfully. Your account is now active.", HttpStatus.ACCEPTED, null));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new BadRequest("Invalid OTP"));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        System.out.println("In resend Otp");
        String otp = authenticationService.resendOtp(email);

        if (otp != null) {
            return ResponseEntity.ok(new SuccessResponse("OTP resend successfully", HttpStatus.OK, null));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InternalServerError("Resend failed"));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        System.out.println("In change password controller");
        String email = changePasswordRequest.getEmail();
        String password = changePasswordRequest.getPassword();
        String rePassword = changePasswordRequest.getRePassword();

        if (!password.equals(rePassword)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequest("Password was re-entered incorrectly"));
        }
        boolean isChanged = authenticationService.changePassword(email, password);
        if (isChanged) {
            return ResponseEntity.ok(new SuccessResponse("Change password successfully", HttpStatus.OK, null));
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Changing password failed by server!"));
        }
    }
}