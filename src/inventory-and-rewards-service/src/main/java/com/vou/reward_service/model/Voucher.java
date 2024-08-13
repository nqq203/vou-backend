package com.vou.reward_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @Column(name = "code")
    String code;

    @Column(name = "qr_code")
    String qrCode;

    @Column(name = "voucher_name")
    String voucherName;

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "expiration_date")
    Timestamp expirationDate;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    String status;

    @Column(name = "id_item1")
    Long idItem1;

    @Column(name = "id_item2")
    Long idItem2;

    @Column(name = "id_item3")
    Long idItem3;

    @Column(name = "id_item4")
    Long idItem4;

    @Column(name = "id_item5")
    Long idItem5;

    @Column(name = "am_coin")
    Long amCoin;

    @Column(name = "id_event")
    Long idEvent;
}
