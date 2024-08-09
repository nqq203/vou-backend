package com.vou.reward_service.controller;

import com.vou.reward_service.entity.CreateItemRequest;
import com.vou.reward_service.entity.CreateVoucherRequest;
import com.vou.reward_service.model.Item;
import com.vou.reward_service.model.Voucher;
import com.vou.reward_service.repository.VoucherRepository;
import com.vou.reward_service.service.ItemService;
import com.vou.reward_service.constant.HttpStatus;
import com.vou.reward_service.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/inventory-and-rewards/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping("")
    public ResponseEntity<List<Voucher>> getVouchers() {
        return ResponseEntity.status(HttpStatus.OK).body(voucherService.getAllVouchers());
    }

    @PostMapping("")
    public ResponseEntity<HashMap<String, Object>>  createVoucher(@RequestBody CreateVoucherRequest request) {
        try {
            int result = voucherService.createVoucher(request);
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

    @GetMapping("/{code}")
    public ResponseEntity<HashMap<String, Object>> getVoucher(@PathVariable("code") String code) {
        try {
            Voucher voucher = voucherService.findVoucherByCode(code);
            HashMap<String, Object> response = new HashMap<>();
            if (voucher == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("description", "Voucher not found");
            }
            else {
                response.put("status", HttpStatus.OK);
                response.put("description", "Voucher details");
                response.put("content", voucher);
            }
            return ResponseEntity.status((int) response.get("status")).body(response);
        } catch (Exception e) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("description", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<HashMap<String, Object>> updateItemById(@PathVariable String code, @RequestBody CreateVoucherRequest request) {
        try {
            Integer result = voucherService.updateVoucherByCode(code, request);
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

    @DeleteMapping("/{code}")
    public ResponseEntity<HashMap<String, Object>> deleteItemById(@PathVariable String code) {
        try {
            Integer result = voucherService.deleteVoucherByCode(code);
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
