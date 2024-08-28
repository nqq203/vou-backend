package com.vou.reward_service.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class InventoryImageDTO {
    MultipartFile qrImg;
    MultipartFile voucherImg;
}
