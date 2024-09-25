package com.vou.user_service.controller;

import com.vou.user_service.common.InternalServerError;
import com.vou.user_service.common.NotFoundException;
import com.vou.user_service.common.NotFoundResponse;
import com.vou.user_service.model.Player;
import com.vou.user_service.model.User;
import com.vou.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
@CrossOrigin
public class PlayerController {
    private final UserService userService;

    public PlayerController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getPlayerByUserId(@PathVariable Long userId) {
        try {
            Player player = userService.findPlayerByUserId(userId);
            return ResponseEntity.ok(player);
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Not found player"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Internal server error: error retrieving player"));
        }
    }

    @PostMapping("/")
    public ResponseEntity<Long> createPlayer(@RequestBody Player player) {
        try {
            Player savedPlayer = userService.createPlayer(player);
            return ResponseEntity.ok(savedPlayer.getIdUser());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{userId}")
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

    @GetMapping("")
    public ResponseEntity<?> findPlayerByIdentifier(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value =  "id_user", required = false) Long idUser
    ) {
        try {
            User player;
            if (email != null) {
                player = userService.findByIdentifier(email);
            } else if (username != null) {
                player = userService.findByIdentifier(username);
            } else {
                player = userService.findByIdUser(idUser);
            }
            return ResponseEntity.ok(player);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi hệ thống khi tìm kiếm user"));
        }
    }
}
