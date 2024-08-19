package com.vou.user_service.controller;

import com.vou.user_service.common.InternalServerError;
import com.vou.user_service.common.NotFoundException;
import com.vou.user_service.common.NotFoundResponse;
import com.vou.user_service.common.SuccessResponse;
import com.vou.user_service.model.Brand;
import com.vou.user_service.model.Player;
import com.vou.user_service.model.User;
import com.vou.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/brands")
@CrossOrigin
public class BrandController {
    private final UserService userService;

    public BrandController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<Boolean> createBrand(@RequestBody Brand brand) {
        try {
            Brand savedBrand = userService.createBrand(brand);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getBrandByUserId(@PathVariable Long userId) {
        try {
            Brand brand = userService.findBrandByUserId(userId);
            return ResponseEntity.ok().body(new SuccessResponse("Get brand successfully", HttpStatus.OK, brand));
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Not found brand"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Internal server error: error retrieving brand"));
        }
    }
}
