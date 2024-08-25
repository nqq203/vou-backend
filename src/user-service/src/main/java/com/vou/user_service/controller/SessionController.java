package com.vou.user_service.controller;


import com.vou.user_service.model.Session;
import com.vou.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
@CrossOrigin
public class SessionController {
    private final UserService userService;

    public SessionController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createSession(@RequestBody Session session) {
        try {
            Session savedSession = userService.createSession(session);
            return new ResponseEntity<>(savedSession, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{token}")
    public ResponseEntity<?> getSessionByToken(@PathVariable String token) {
        Session session = userService.findSessionByToken(token);
        if (session != null) {
            return ResponseEntity.ok(session);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{token}")
    public ResponseEntity<?> updateSession(@PathVariable String token, @RequestBody Session session) {
        Session existSession = userService.findSessionByToken(token);
        if (existSession == null) {
            return ResponseEntity.notFound().build();
        }
        Session savedSession = userService.updateSession(session);
        return new ResponseEntity<>(savedSession, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getListSession() {
        List<Session> sessions = userService.findAll();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }
}
