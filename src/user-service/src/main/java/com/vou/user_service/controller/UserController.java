package com.vou.user_service.controller;

import com.vou.user_service.common.*;
import com.vou.user_service.model.Brand;
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
import java.util.Arrays;
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
        String contentType = avatarFile.getContentType();
        if (!Arrays.asList("image/png", "image/jpeg", "image/jpg").contains(contentType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("File không hợp lệ", HttpStatus.BAD_REQUEST, "Chỉ cho phép các định dạng png, jpg, jpeg"));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequest("User id không hợp lệ."));
        }
        if (!user.getRole().toString().equalsIgnoreCase("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ForbiddenResponse("Chỉ admin mới có thể sử dụng chức năng này."));
        }
        try {
            List<User> users = userService.findAllUsers();
            if (users == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Danh sách người dùng rỗng"));
            }
            return ResponseEntity.ok().body(new SuccessResponse("Truy cập danh sách người dùng thành công", HttpStatus.OK, users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi hệ thống khi cố gắng truy cập danh sách người dùng!"));
        }
    }

    @GetMapping("/query")
    public ResponseEntity<?> getUserByUsernameAndEmail(@RequestParam("username") String username, @RequestParam("email") String email) {
        try {
            System.out.println("username :" + username);
            System.out.println("email: " + email);
            User existUser = userService.findUserByUsernameAndEmail(username, email);
            System.out.println("Trên đây!!");
            System.out.println("User nè: " + existUser);
            if (existUser == null) {
                return ResponseEntity.notFound().build();
            }
            System.out.println("Xuống đây!!");
            return ResponseEntity.ok(existUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống khi kiểm tra người dùng tồn tại");
        }
    }

    @GetMapping("/{id_user}/events")
    public ResponseEntity<?> getBrandLogo(@PathVariable Long id_user) {
        try {
            User user = userService.findByIdUser(id_user);
            return ResponseEntity.ok(user.getAvatarUrl());
        } catch (NotFoundException e) {
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/statistics/users")
    public ResponseEntity<?> getListUserForStatistics(@RequestBody List<String> usernames) {
        try {
            List<User> users = userService.findUsersByUsernames(usernames);
            if (users == null || users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(users);
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi hệ thống khi cố gắng truy cập danh sách người dùng!"));
        }
    }
}