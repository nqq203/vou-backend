package com.vou.reward_service.controller;

import com.vou.reward_service.entity.CreateItemRequest;
import com.vou.reward_service.model.Item;
import com.vou.reward_service.service.ItemService;
import com.vou.reward_service.constant.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@CrossOrigin
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("")
    public ResponseEntity<List<Item>> getItem() {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getAllItems());
    }

    @PostMapping("")
    public ResponseEntity<HashMap<String, Object>>  createItem(@RequestBody CreateItemRequest request) {
        try {
            int result = itemService.createItem(request);
            HashMap<String, Object> response = new HashMap<>();
            if (result == HttpStatus.CREATED) {
                response.put("status", HttpStatus.CREATED);
                response.put("description", "Item created");
            } else if (result == HttpStatus.INTERNAL_SERVER_ERROR) {
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
                response.put("description", "Internal server error");
            }
            return ResponseEntity.status((int) response.get("status")).body(response);
        } catch (Exception e) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("description", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id_item}")
    public ResponseEntity<HashMap<String, Object>> getItemById(@PathVariable Long id_item) {
        try {
            Item result = itemService.getItemById(id_item);
            HashMap<String, Object> response = new HashMap<>();
            if (result == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("description", "Item not found");
            }
            else {
                response.put("status", HttpStatus.OK);
                response.put("description", "Item details");
                response.put("content", result);
            }
            return ResponseEntity.status((int) response.get("status")).body(response);
        } catch (Exception e) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("description", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id_item}")
    public ResponseEntity<HashMap<String, Object>> updateItemById(@PathVariable Long id_item, @RequestBody CreateItemRequest request) {
        try {
            Integer result = itemService.updateItemById(id_item, request);
            HashMap<String, Object> response = new HashMap<>();
            if (result == HttpStatus.NOT_FOUND) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("description", "Item not found");
            } else if (result == HttpStatus.INTERNAL_SERVER_ERROR) {
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
                response.put("description", "Internal server error");
            } else {
                response.put("status", HttpStatus.OK);
                response.put("description", "Item updated");
            }
            return ResponseEntity.status((int) response.get("status")).body(response);
        } catch (Exception e) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("description", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id_item}")
    public ResponseEntity<HashMap<String, Object>> deleteItemById(@PathVariable Long id_item) {
        try {
            Integer result = itemService.deleteItemById(id_item);
            HashMap<String, Object> response = new HashMap<>();
            if (result == HttpStatus.NOT_FOUND) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("description", "Item not found");
            } else if (result == HttpStatus.INTERNAL_SERVER_ERROR) {
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
                response.put("description", "Internal server error");
            } else {
                response.put("status", HttpStatus.OK);
                response.put("description", "Item updated");
            }
            return ResponseEntity.status((int) response.get("status")).body(response);
        } catch (Exception e) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("description", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
