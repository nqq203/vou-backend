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

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/get-user-by-username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get-player-by-userid/{userId}")
    public ResponseEntity<Player> getPlayerByIdUser(@PathVariable Long userId) {
        Player player = userService.findPlayerByUserId(userId);
        if (player != null) {
            return ResponseEntity.ok(player);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/get-user-by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-admin")
    public ResponseEntity<Boolean> createAdmin(@RequestBody Admin admin) {
        Admin savedAdmin = userService.createAdmin(admin);
        if (savedAdmin != null) {
            return ResponseEntity.ok(true);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create-player")
    public ResponseEntity<Boolean> createPlayer(@RequestBody Player player) {
        Player savedPlayer = userService.createPlayer(player);
        if (savedPlayer != null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create-brand")
    public ResponseEntity<Boolean> createBrand(@RequestBody Brand brand) {
        Brand savedBrand = userService.createBrand(brand);
        if (savedBrand != null) {
            return ResponseEntity.ok(true);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/update-player")
    public ResponseEntity<?> updatePlayer(@RequestBody Player player) {
        Player savedPlayer = userService.updatePlayer(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.OK);
    }

    @PostMapping("/update-user-internal")
    public ResponseEntity<Boolean> updateUserInternal(@RequestBody User user) {
        User savedUser = userService.updateUserInternal(user);
        if (savedUser != null) {
            return ResponseEntity.ok(true);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create-session")
    public ResponseEntity<?> createSession(@RequestBody Session session) {
        Session savedSession = userService.createSession(session);
        return new ResponseEntity<>(savedSession, HttpStatus.OK);
    }

    @GetMapping("/get-token/{token}")
    public ResponseEntity<?> getSessionByToken(@PathVariable String token) {
        Session session = userService.findSessionByToken(token);
        if (session != null) {
            return ResponseEntity.ok(session);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/update-session")
    public ResponseEntity<?> updateSession(@RequestBody Session session) {
        Session savedSession = userService.updateSession(session);
        return new ResponseEntity<>(savedSession, HttpStatus.OK);
    }

    @GetMapping("/get-all-sessions")
    public ResponseEntity<?> getListSession() {
        List<Session> sessions = userService.findAll();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }
}