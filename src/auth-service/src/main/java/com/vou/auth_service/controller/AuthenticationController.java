package com.vou.auth_service.controller;

import com.vou.auth_service.common.*;
import com.vou.auth_service.constant.Role;
import com.vou.auth_service.constant.Status;
import com.vou.auth_service.entity.*;
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
        System.out.println("Attempting to log in user: " + loginRequest.getUsername());
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body(new BadRequest("Not found username or password"));
        }

        String token = authenticationService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid username or password",
                            HttpStatus.UNAUTHORIZED, null));
        }

        if (token.equals("1")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Unverified account, please verify OTP",
                            HttpStatus.UNAUTHORIZED, null));
        }

        if (token.equals("2")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Unverified account, please waiting for account approval",
                            HttpStatus.UNAUTHORIZED, null));
        }


        User user = authenticationService.loadUserByUsername(loginRequest.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Handle get user failed"));
        }

        Object account;
        if (user.getRole().toString().equalsIgnoreCase("admin")) {
            account = authenticationService.getAdminById(user.getIdUser());
        }
        else if (user.getRole().toString().equalsIgnoreCase("player")) {
            account = authenticationService.getPlayerById(user.getIdUser());
        }
        else {
            account = authenticationService.getBrandById(user.getIdUser());
        }

        LoginResponse loginResponse = new LoginResponse(token, account);
        return ResponseEntity.ok(new SuccessResponse("Login successfully", HttpStatus.OK, loginResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        System.out.println("Attempting to login user: " + registerRequest.getUsername());

//        implement logic to check role
        Role role;
        if (registerRequest.getRole().equalsIgnoreCase("player")) {
            role = Role.PLAYER;
        }
        else if (registerRequest.getRole().equalsIgnoreCase("admin")) {
            role = Role.ADMIN;
        }
        else {
            role = Role.BRAND;
        }

        User newUser = new User(registerRequest.getUsername(),
                                registerRequest.getPassword(),
                                registerRequest.getFullName(),
                                registerRequest.getEmail(),
                                registerRequest.getPhoneNumber(),
                                role,
                                Status.PENDING);

        boolean result = authenticationService.register(newUser);
        if (result) {
            return ResponseEntity.ok(new SuccessResponse(
                    "User registered successfully",
                    HttpStatus.CREATED, null));
        } else {
            return ResponseEntity.badRequest().body(new BadRequest("Username or email already exists"));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token, @RequestBody LogoutRequest logoutRequest) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        boolean result = authenticationService.logout(token, logoutRequest.getIdUser());
        System.out.println("logout boolean:" + result);
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
        if (otp.length() != 6) {
            return ResponseEntity.badRequest().body(new BadRequest("Invalid OTP"));
        }
        boolean isVerified = authenticationService.verifyOtp(username, otp);
        System.out.println(isVerified);
        if (isVerified) {
            String token = authenticationService.loginWithoutPassword(username);
            User user = authenticationService.loadUserByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Handle get user failed"));
            }
            if (token != null) {
                Object account = authenticationService.getPlayerById(user.getIdUser());
                LoginResponse loginResponse = new LoginResponse(token, account);
                return ResponseEntity.ok(new SuccessResponse("OTP verified and login successfully. Your account is now active.", HttpStatus.CREATED, loginResponse));
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Create token failed"));
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new BadRequest("Invalid OTP"));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody ResendOtpRequest resendOtpRequest) {
        System.out.println("In resend Otp");
        String otp = authenticationService.resendOtp(resendOtpRequest.getUsername(), null);

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

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        System.out.println("In validate token");
        System.out.println(token);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        System.out.println(token);

        boolean isValidToken = authenticationService.validateToken(token);
        return ResponseEntity.ok(isValidToken);
    }
}