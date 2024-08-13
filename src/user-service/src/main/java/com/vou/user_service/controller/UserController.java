package com.vou.user_service.controller;

import com.vou.user_service.common.ErrorResponse;
import com.vou.user_service.common.NotFoundException;
import com.vou.user_service.common.NotFoundResponse;
import com.vou.user_service.common.SuccessResponse;
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

    @GetMapping("/players/{userId}")
    public ResponseEntity<Player> getPlayerByIdUser(@PathVariable Long userId) {
        try {
            Player player = userService.findPlayerByUserId(userId);
            return ResponseEntity.ok(player);
        } catch (NotFoundException notFoundException) {
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

    @PostMapping("/admins")
    public ResponseEntity<Boolean> createAdmin(@RequestBody Admin admin) {
        try {
            Admin savedAdmin = userService.createAdmin(admin);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/players")
    public ResponseEntity<Boolean> createPlayer(@RequestBody Player player) {
        try {
            Player savedPlayer = userService.createPlayer(player);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/brands")
    public ResponseEntity<Boolean> createBrand(@RequestBody Brand brand) {
        try {
            Brand savedBrand = userService.createBrand(brand);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/players/{userId}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long userId, @RequestBody Player player) {
        try {
            User existUser = userService.findByIdUser(userId);
            Player savedPlayer = userService.updatePlayer(player);
            return new ResponseEntity<>(savedPlayer, HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/sessions")
    public ResponseEntity<?> createSession(@RequestBody Session session) {
        try {
            Session savedSession = userService.createSession(session);
            return new ResponseEntity<>(savedSession, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/sessions/{token}")
    public ResponseEntity<?> getSessionByToken(@PathVariable String token) {
        Session session = userService.findSessionByToken(token);
        if (session != null) {
            return ResponseEntity.ok(session);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/sessions/{token}")
    public ResponseEntity<?> updateSession(@PathVariable String token, @RequestBody Session session) {
        Session existSession = userService.findSessionByToken(token);
        if (existSession == null) {
            return ResponseEntity.notFound().build();
        }
        Session savedSession = userService.updateSession(session);
        return new ResponseEntity<>(savedSession, HttpStatus.OK);
    }

    @GetMapping("/sessions")
    public ResponseEntity<?> getListSession() {
        List<Session> sessions = userService.findAll();
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }
}