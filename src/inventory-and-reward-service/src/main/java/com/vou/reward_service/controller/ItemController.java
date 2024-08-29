package com.vou.reward_service.controller;

import com.vou.reward_service.common.*;
import com.vou.reward_service.entity.CreateItemRequest;
import com.vou.reward_service.model.Item;
import com.vou.reward_service.service.ItemService;
//import com.vou.reward_service.constant.HttpStatus;
import com.vou.reward_service.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/items")
@CrossOrigin
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getItem() {
        List<Item> items = itemService.getAllItems();
        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new NotFoundResponse("Không tìm thấy danh sách item"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("Danh sách tất cả các item", HttpStatus.OK, items));
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> createItem(@RequestBody CreateItemRequest request) {
        try {
            int result = itemService.createItem(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SuccessResponse("Đã tạo thành công 1 item", HttpStatus.CREATED.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerError("Lỗi khi tạo item"));
        }
    }

    @GetMapping("/{id_item}")
    public ResponseEntity<ApiResponse> getItemById(@PathVariable Long id_item) {
        try {
            Item result = itemService.getItemById(id_item);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new NotFoundResponse("Không tìm thấy item bằng id"));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse("Tìm thấy item bằng id", HttpStatus.OK, result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerError("Lỗi lấy item bằng id"));
        }
    }

    @PutMapping("/{id_item}")
    public ResponseEntity<ApiResponse> updateItemById(@PathVariable Long id_item,
            @RequestBody CreateItemRequest request) {
        try {
            Integer result = itemService.updateItemById(id_item, request);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse("Đã update thành công item bằng id", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerError(e.getMessage()));
        }
    }

    @DeleteMapping("/{id_item}")
    public ResponseEntity<ApiResponse> deleteItemById(@PathVariable Long id_item) {
        try {
            Integer result = itemService.deleteItemById(id_item);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerError(e.getMessage()));
        }
    }

    @GetMapping("/events/{id_event}")
    public ResponseEntity<ApiResponse> getItemsByEvent(@PathVariable Long id_event) {
        try {
            Set<Item> items = itemService.getListItemsOfEvent(id_event);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Các items cần cho sự kiện này là", HttpStatus.OK, items));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(e.getMessage()));
        }
    }
}
