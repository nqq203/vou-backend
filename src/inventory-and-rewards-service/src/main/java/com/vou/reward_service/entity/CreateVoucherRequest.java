package com.vou.reward_service.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CreateVoucherRequest {
    String code;

    String qrCode;

    String voucherName;

    String imageUrl;

    Timestamp expirationDate;

    String description;

    String status;

    Long idItem1;

    Long idItem2;

    Long idItem3;

    Long idItem4;

    Long idItem5;

    Long amCoin;

    Long idEvent;
}
