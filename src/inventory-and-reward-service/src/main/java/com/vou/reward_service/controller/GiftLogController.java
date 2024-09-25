package com.vou.reward_service.controller;

import com.vou.reward_service.common.ApiResponse;
import com.vou.reward_service.common.InternalServerError;
import com.vou.reward_service.common.SuccessResponse;
import com.vou.reward_service.entity.GiftItemRequest;
import com.vou.reward_service.model.GiftLog;
import com.vou.reward_service.service.GiftLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gifts")
@CrossOrigin
public class GiftLogController {
    @Autowired
    private GiftLogService giftLogService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> giftAnItem(@RequestBody GiftItemRequest request) {
        try {
            giftLogService.giftAnItemForUser(request);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Gửi thành công vật phẩm", HttpStatus.CREATED.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(e.getMessage()));
        }
    }

    @GetMapping("/users/{id_user}")
    public ResponseEntity<ApiResponse> getGiftingHistoryByUserId(@PathVariable Long id_user) {
        try {
            HashMap<String, Object> giftingHistory = giftLogService.getGiftingHistoryByUserId(id_user);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Lấy thành công lịch sử tặng quà", HttpStatus.OK, giftingHistory));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(e.getMessage()));
        }
    }
}
