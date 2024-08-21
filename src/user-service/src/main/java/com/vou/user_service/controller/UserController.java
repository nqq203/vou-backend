package com.vou.user_service.controller;

import com.vou.user_service.common.*;
import com.vou.user_service.model.User;
import com.vou.user_service.service.StorageService;
import com.vou.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final StorageService storageService;

    @Autowired
    public UserController(UserService userService, StorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
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

    @PatchMapping ("/{id_user}/avatar")
    public ResponseEntity<?> updateAvatar(@PathVariable Long id_user, @RequestParam("avatar") MultipartFile avatarFile) {
        if (!avatarFile.getContentType().startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid file type", HttpStatus.BAD_REQUEST, "Only image files are allowed"));
        }
        try {
            String avatarUrl = storageService.uploadImage(avatarFile);
            if (avatarUrl == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error uploading", HttpStatus.INTERNAL_SERVER_ERROR, null));
            }
            Boolean isUpdated = userService.updateAvatarUser(id_user, avatarUrl);
            if (!isUpdated) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error updating avatar url", HttpStatus.INTERNAL_SERVER_ERROR, null));
            }
            return ResponseEntity.ok(new SuccessResponse("Avatar updated successfully", HttpStatus.OK, avatarUrl));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Database error", HttpStatus.INTERNAL_SERVER_ERROR, null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("File upload error", HttpStatus.INTERNAL_SERVER_ERROR, null));
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

    @GetMapping("")
    public ResponseEntity<?> getListUser(@RequestParam("id_user") Long id_user) {
        User user;
        try {
            user = userService.findByIdUser(id_user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequest("Bad Request: Invalid user id"));
        }
        if (!user.getRole().toString().equalsIgnoreCase("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ForbiddenResponse("Access Denied: Just admin can get list users"));
        }
        try {
            List<User> users = userService.findAllUsers();
            if (users == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Empty users list"));
            }
            return ResponseEntity.ok().body(new SuccessResponse("Get list users successfully", HttpStatus.OK, users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Get list users failed by server"));
        }
    }
}