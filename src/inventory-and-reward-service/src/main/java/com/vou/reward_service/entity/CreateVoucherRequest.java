package com.vou.reward_service.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class CreateVoucherRequest {
    String code;

    String qrCode;

    String voucherName;

    String imageUrl;

    Timestamp expirationDate;

    String description;

    String type;

    Long idItem1;

    Long idItem2;

    Long idItem3;

    Long idItem4;

    Long idItem5;

    Long aimCoin;

    Long idEvent;

    public CreateVoucherRequest(
            String code,
            String qrCode,
            String voucherName,
            String imageUrl,
            Timestamp expirationDate,
            String description,
            String type,
            List<Long> itemId,
            Long aimCoin,
            Long idEvent
    ) {
        this.code = code;
        this.qrCode = qrCode;
        this.voucherName = voucherName;
        this.imageUrl = imageUrl;
        this.expirationDate = expirationDate;
        this.description = description;
        this.type = type;
        this.aimCoin = aimCoin;
        this.idEvent = idEvent;

        if (itemId != null && !itemId.isEmpty()) {
            if (itemId.size() > 0) this.idItem1 = itemId.get(0);
            if (itemId.size() > 1) this.idItem2 = itemId.get(1);
            if (itemId.size() > 2) this.idItem3 = itemId.get(2);
            if (itemId.size() > 3) this.idItem4 = itemId.get(3);
            if (itemId.size() > 4) this.idItem5 = itemId.get(4);
        }
    }

    public CreateVoucherRequest(
            String code,
            String qrCode,
            String voucherName,
            String imageUrl,
            Timestamp expirationDate,
            String description,
            String type,
            Long idEvent
    ) {
        this.code = code;
        this.qrCode = qrCode;
        this.voucherName = voucherName;
        this.imageUrl = imageUrl;
        this.expirationDate = expirationDate;
        this.description = description;
        this.type = type;
        this.idEvent = idEvent;
    }
}
