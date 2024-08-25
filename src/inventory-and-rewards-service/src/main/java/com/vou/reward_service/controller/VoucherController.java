package com.vou.reward_service.controller;

import com.vou.reward_service.dto.InventoryDTO;
import com.vou.reward_service.dto.InventoryDetailDTO;
import com.vou.reward_service.dto.ItemDetailDTO;
import com.vou.reward_service.entity.CreateItemRequest;
import com.vou.reward_service.entity.CreateVoucherRequest;
import com.vou.reward_service.model.Item;
import com.vou.reward_service.model.Voucher;
import com.vou.reward_service.repository.ItemRepository;
import com.vou.reward_service.repository.VoucherRepository;
import com.vou.reward_service.service.ItemService;
import com.vou.reward_service.constant.HttpStatus;
import com.vou.reward_service.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/inventory-and-rewards/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("")
    public ResponseEntity<List<Voucher>> getVouchers() {
        return ResponseEntity.status(HttpStatus.OK).body(voucherService.getAllVouchers());
    }

    @PostMapping("")
    public ResponseEntity<HashMap<String, Object>>  createVoucher(@RequestBody InventoryDTO inventoryDTO) {
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

    private void addItemToList(Long itemId, List<Item> items) {
        if (itemId != null) {
            Optional<Item> item = itemRepository.findById(itemId);
            item.ifPresent(items::add);
        }
    }
    @GetMapping("/voucher-info")
    public ResponseEntity<InventoryDetailDTO> getInventoryInfo(@RequestParam Long eventId){
        Voucher voucher = voucherRepository.findByIdEvent(eventId);
        List<Item> items = new ArrayList<>();
        addItemToList(voucher.getIdItem1(), items);
        addItemToList(voucher.getIdItem2(), items);
        addItemToList(voucher.getIdItem3(), items);
        addItemToList(voucher.getIdItem4(), items);
        addItemToList(voucher.getIdItem5(), items);
        List<ItemDetailDTO> itemDetailDTOS = items.stream()
                .map(ItemDetailDTO::new)
                .collect(Collectors.toList());

        InventoryDetailDTO result = new InventoryDetailDTO(
                voucher.getType(),
                voucher.getCode(),
                voucher.getDescription(),
                voucher.getVoucherName(),
                voucher.getVoucherPrice(),
                voucher.getAimCoin(),
                voucher.getExpirationDate(),
                itemDetailDTOS,
                voucher.getIdEvent()
        );

        return ResponseEntity.ok(result);
    }

}
