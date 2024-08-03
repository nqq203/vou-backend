package com.vou.user_service.controller;

import com.vou.user_service.common.SuccessResponse;
import com.vou.user_service.model.*;
import com.vou.user_service.service.UserService;
import jakarta.ws.rs.Path;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody Map<String, Object> updates, @PathVariable Long userId) {
        try {
            User updatedUser = userService.updateUser(userId, updates);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/players/{userId}")
    public ResponseEntity<Player> getPlayerByIdUser(@PathVariable Long userId) {
        Player player = userService.findPlayerByUserId(userId);
        if (player != null) {
            return ResponseEntity.ok(player);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/admin")
    public ResponseEntity<Boolean> createAdmin(@RequestBody Admin admin) {
        Admin savedAdmin = userService.createAdmin(admin);
        if (savedAdmin != null) {
            return ResponseEntity.ok(true);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/player")
    public ResponseEntity<Boolean> createPlayer(@RequestBody Player player) {
        Player savedPlayer = userService.createPlayer(player);
        if (savedPlayer != null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/brand")
    public ResponseEntity<Boolean> createBrand(@RequestBody Brand brand) {
        Brand savedBrand = userService.createBrand(brand);
        if (savedBrand != null) {
            return ResponseEntity.ok(true);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/player/{userId}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long userId, @RequestBody Player player) {
        User existUser = userService.findByIdUser(userId);
        if (existUser == null) {
            return ResponseEntity.notFound().build();
        }
        Player savedPlayer = userService.updatePlayer(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.OK);
    }

    @PutMapping("/{userId}/internal-update")
    public ResponseEntity<Boolean> updateUserInternal(@PathVariable Long userId, @RequestBody User user) {
        User existUser = userService.findByIdUser(userId);
        if (existUser == null) {
            return ResponseEntity.notFound().build();
        }

        existUser.setIdUser(user.getIdUser());
        existUser.setUsername(user.getUsername());
        existUser.setPassword(user.getPassword());
        existUser.setFullName(user.getFullName());
        existUser.setEmail(user.getEmail());
        existUser.setPhoneNumber(user.getPhoneNumber());
        existUser.setLockedDate(user.getLockedDate());
        existUser.setRole(user.getRole());
        existUser.setStatus(user.getStatus());

        User savedUser = userService.updateUserInternal(existUser);
        if (savedUser != null) {
            return ResponseEntity.ok(true);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestBody Session session) {
        Session savedSession = userService.createSession(session);
        return new ResponseEntity<>(savedSession, HttpStatus.OK);
    }

    @GetMapping("/session/{token}")
    public ResponseEntity<?> getSessionByToken(@PathVariable String token) {
        Session session = userService.findSessionByToken(token);
        if (session != null) {
            return ResponseEntity.ok(session);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/session/{token}")
    public ResponseEntity<?> updateSession(@PathVariable String token, @RequestBody Session session) {
        Session existSession = userService.findSessionByToken(token);
        if (existSession == null) {
            return ResponseEntity.notFound().build();
        }
        Session savedSession = userService.updateSession(session);
        return new ResponseEntity<>(savedSession, HttpStatus.OK);
    }

    @GetMapping("/session")
    public ResponseEntity<?> getListSession() {
        List<Session> sessions = userService.findAll();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }
}