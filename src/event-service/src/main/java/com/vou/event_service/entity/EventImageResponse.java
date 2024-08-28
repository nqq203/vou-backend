package com.vou.event_service.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventImageResponse {
    String bannerUrl;
    String qrImgUrl;
    String voucherImgUrl;

    public EventImageResponse(String bannerUrl, String qrImgUrl, String voucherImgUrl) {
        this.bannerUrl = bannerUrl;
        this.qrImgUrl = qrImgUrl;
        this.voucherImgUrl = voucherImgUrl;
    }
}
