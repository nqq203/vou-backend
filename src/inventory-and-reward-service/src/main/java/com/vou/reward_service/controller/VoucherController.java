package com.vou.reward_service.controller;

import com.vou.reward_service.dto.InventoryDTO;
import com.vou.reward_service.common.*;
import com.vou.reward_service.dto.*;
import com.vou.reward_service.entity.CreateVoucherRequest;
import com.vou.reward_service.entity.RewardVouchersForWinnersRequest;
import com.vou.reward_service.entity.UserVoucher;
import com.vou.reward_service.model.Item;
import com.vou.reward_service.model.Voucher;
import com.vou.reward_service.service.StorageService;
import com.vou.reward_service.service.VoucherRepoService;
import org.springframework.http.HttpStatus;
import com.vou.reward_service.repository.ItemRepository;
import com.vou.reward_service.repository.VoucherRepository;
import com.vou.reward_service.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private VoucherRepoService voucherRepoService;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private StorageService storageService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getVouchers(@RequestParam("type") String type) {
        if (!Arrays.asList("online", "offline").contains(type)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequest("Không tồn tại loại voucher"));
        }
        try {
            List<Voucher> vouchers = voucherService.getListOnlineOrOfflineVoucher(type);
            if (vouchers == null) {
                return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi hệ thống khi tìm danh sách voucher bằng " + type));
            }
            if (vouchers.toArray().length == 0) {
                return ResponseEntity.ok(new SuccessResponse("Hiện không có vouchers " + type + " nào", HttpStatus.OK, null));
            }
            return ResponseEntity.ok(new SuccessResponse("Truy cập danh sách voucher " + type, HttpStatus.OK, vouchers));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi hệ thống khi truy cập danh sách voucher " + type));
        }
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
                        inventoryDTO.getVoucher_price(),
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
                        inventoryDTO.getVoucher_price(),
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
    public ResponseEntity<ApiResponse> getVoucher(@PathVariable("code") String code) {
        if (code == null) {
            return ResponseEntity.badRequest().body(new BadRequest("Voucher code không hợp lệ"));
        }
        try {
            Voucher voucher = voucherService.findVoucherByCode(code);
            if (voucher == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Không tìm thấy voucher với code tương ứng"));
            }
            return ResponseEntity.ok(new SuccessResponse("Truy cập thông tin voucher thành công!" , HttpStatus.OK, voucher));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi hệ thống khi cố gắng truy cập thông tin voucher!"));
        }
    }

    @PutMapping("/info")
    public ResponseEntity<ApiResponse> updateVoucher(@RequestBody InventoryDTO inventoryDTO, @RequestParam String code) {
        try {
            CreateVoucherRequest request;
            if (inventoryDTO.getGameType().equals("shake-game")) {
                request = new CreateVoucherRequest(
                        inventoryDTO.getVoucher_code(),
                        null,
                        inventoryDTO.getVoucher_name(),
                        null,
                        inventoryDTO.getExpiration_date(),
                        inventoryDTO.getVoucher_price(),
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
                        inventoryDTO.getVoucher_price(),
                        inventoryDTO.getVoucher_description(),
                        inventoryDTO.getVoucher_type(),
                        inventoryDTO.getEvent_id()
                );
            }

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
    public ResponseEntity<ApiResponse> getUserVouchers(@PathVariable Long id, @RequestParam String type) {
        if (type == null || !Arrays.asList("online", "offline").contains(type)) {
            return ResponseEntity.badRequest().body(new BadRequest("Loại voucher không hợp lệ!"));
        }
        try {
            List<UserVoucher> userVouchers = voucherService.getVouchersByUserId(id, type);
            ApiResponse response = new SuccessResponse("Truy cập danh sách vouchers " + type + " thành công!", HttpStatus.OK, userVouchers);
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
  
    private void addItemToList(Long itemId, List<Item> items) {
        if (itemId != null) {
            Optional<Item> item = itemRepository.findById(itemId);
            item.ifPresent(items::add);
        }
    }
  
    @GetMapping("/events/{eventId}")
    public ResponseEntity<InventoryDetailDTO> getInventoryInfo(@PathVariable Long eventId) {
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
                voucher.getImageUrl(),
                voucher.getQrCode(),
                voucher.getAimCoin(),
                voucher.getExpirationDate(),
                itemDetailDTOS,
                voucher.getIdEvent()
        );

        return ResponseEntity.ok(result);
    }

   @PutMapping("")
   public ResponseEntity<InventoryImageUrlDTO> uploadInventoryImages(
           @RequestParam("id_event") Long id_event,
           @ModelAttribute InventoryImageDTO inventoryImages
   ) {
       if (!isImageFile(inventoryImages.getQrImg()) || !isImageFile(inventoryImages.getVoucherImg())) {
           return ResponseEntity.badRequest().build();
       }
       Voucher existVoucher;
       try {
           existVoucher = voucherService.findVoucherByIdEvent(id_event);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
       if (existVoucher == null) {
           return ResponseEntity.notFound().build();
       }

       try {
           String qrImgUrl = storageService.uploadImage(inventoryImages.getQrImg(), "qr_code");
           String voucherImgUrl = storageService.uploadImage(inventoryImages.getVoucherImg(), "voucher");

           if (qrImgUrl == null || voucherImgUrl == null) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
           }
           boolean isUploaded = voucherService.uploadInventoryImages(existVoucher, qrImgUrl, voucherImgUrl);
           if (!isUploaded) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
           }
           return ResponseEntity.ok(new InventoryImageUrlDTO(qrImgUrl, voucherImgUrl));
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
   }

   private boolean isImageFile(MultipartFile file) {
       return file != null && file.getContentType() != null && file.getContentType().startsWith("image/");
   }

    @GetMapping("/exists/{voucherCode}")
    public ResponseEntity<Boolean> checkVoucherExists(@PathVariable String voucherCode) {
        // Check if the voucher exists in the repository
        boolean exists = voucherRepository.existsByCode(voucherCode.toUpperCase());
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/gifts")
    public ResponseEntity<ApiResponse> rewardVouchersForWinners(@RequestBody RewardVouchersForWinnersRequest request) {
        try {
            voucherRepoService.rewardVoucherQuizGame(request.getWinnerIds(), request.getVoucherCode());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Đã gửi phần thưởng vào kho voucher của người chơi", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(e.getMessage()));
        }
    }
}
