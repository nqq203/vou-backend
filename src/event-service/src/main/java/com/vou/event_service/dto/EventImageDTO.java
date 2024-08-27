package com.vou.event_service.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class EventImageDTO {
    private MultipartFile bannerFile;
    private MultipartFile qrImage;
    private MultipartFile voucherImg;
}
