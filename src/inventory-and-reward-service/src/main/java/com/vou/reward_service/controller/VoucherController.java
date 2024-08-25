package com.vou.reward_service.controller;

import com.vou.reward_service.common.*;
import com.vou.reward_service.dto.InventoryDTO;
import com.vou.reward_service.entity.CreateVoucherRequest;
import com.vou.reward_service.entity.UserVoucher;
import com.vou.reward_service.model.Voucher;
import com.vou.reward_service.service.VoucherRepoService;
import org.springframework.http.HttpStatus;
import com.vou.reward_service.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/inventory-and-reward/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private VoucherRepoService voucherRepoService;

    @GetMapping("")
    public ResponseEntity<List<Voucher>> getVouchers() {
        return ResponseEntity.status(HttpStatus.OK).body(voucherService.getAllVouchers());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse>  createVoucher(@RequestBody InventoryDTO inventoryDTO) {
        try {
            CreateVoucherRequest request;
            if (inventoryDTO.getGameType().equals("shake-game")) {
                request = new CreateVoucherRequest(
                        inventoryDTO.getVoucher_code(),
                        null,
                        inventoryDTO.getVoucher_name(),
                        null,
                        inventoryDTO.getExpiration_date(),
                        inventoryDTO.getVoucher_description(),
                        inventoryDTO.getVoucher_type(),
                        inventoryDTO.getItems(),
                        inventoryDTO.getAim_coin(),
                        inventoryDTO.getEvent_id()
                );
            }
            else {
                request = new CreateVoucherRequest(
                        inventoryDTO.getVoucher_code(),
                        null,
                        inventoryDTO.getVoucher_name(),
                        null,
                        inventoryDTO.getExpiration_date(),
                        inventoryDTO.getVoucher_description(),
                        inventoryDTO.getVoucher_type(),
                        inventoryDTO.getEvent_id()
                );
            }
            int result = voucherService.createVoucher(request);
            ApiResponse response = new CreatedResponse("Khởi tạo thành công voucher");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError());
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
    public ResponseEntity<ApiResponse> updateVoucherById(@PathVariable String code, @RequestBody CreateVoucherRequest request) {
        try {
            Integer result = voucherService.updateVoucherByCode(code, request);
            ApiResponse response = new SuccessResponse("Cập nhật voucher thành công", 200);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi server khi cập nhật voucher"));
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse> deleteVoucherById(@PathVariable String code) {
        try {
            Integer result = voucherService.deleteVoucherByCode(code);
            ApiResponse response = new SuccessResponse("Xóa voucher thành công", HttpStatus.OK, result);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi server khi xoá voucher"));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUserVouchers(@PathVariable Long id) {
        try {
            List<UserVoucher> userVouchers = voucherService.getVouchersByUserId(id);
            ApiResponse response = new SuccessResponse("Tất cả voucher của người dùng", HttpStatus.OK, userVouchers);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException notFoundE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Không tìm thấy voucher nào của người dùng"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(e.getMessage()));
        }
    }

    @PostMapping("{code}/users/{id}")
    public ResponseEntity<ApiResponse> exchangeVoucherByItem(@PathVariable("code") String code, @PathVariable("id") Long id) {
        try {
            boolean result = voucherRepoService.exchangeItem(code, id);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Đổi voucher thành công", 201));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(e.getMessage()));
        }
    }
}
