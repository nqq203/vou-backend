package com.vou.user_service.controller;

import com.vou.user_service.common.*;
import com.vou.user_service.model.Admin;
import com.vou.user_service.model.Brand;
import com.vou.user_service.model.Player;
import com.vou.user_service.model.User;
import com.vou.user_service.model.Session;
import com.vou.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{id_user}")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> updates, @PathVariable("id_user") Long id_user) {
        try {
            User updatedUser = userService.updateUser(id_user, updates);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("User updated", HttpStatus.OK, updatedUser));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("User not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e));
        }
    }

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("identifier") String identifier) {
        try {
            User user = userService.findByIdentifier(identifier);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    // TODO: replaced by get by identifier api above
//    @GetMapping("/by-email/{email}")
//    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
//        User user = userService.findByEmail(email);
//        if (user != null){
//            return ResponseEntity.ok(user);
//        }
//        return ResponseEntity.notFound().build();
//    }
}