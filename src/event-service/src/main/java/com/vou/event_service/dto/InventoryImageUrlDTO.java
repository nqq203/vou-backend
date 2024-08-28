package com.vou.event_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryImageUrlDTO {
    String qrImgUrl;
    String voucherImgUrl;

    public InventoryImageUrlDTO(String qrImgUrl, String voucherImgUrl) {
        this.qrImgUrl = qrImgUrl;
        this.voucherImgUrl = voucherImgUrl;
    }
}
