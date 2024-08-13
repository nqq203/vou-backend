package com.vou.reward_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "giftlog")
public class GiftLog {
    @Id
    @Column(name = "id_giftlog")
    Long idGiftLog;

    @Column(name = "uid_receiver")
    Long uidReceiver;

    @Column(name = "uid_sender")
    Long uidSender;

    @Column(name = "id_item")
    Long idItem;

    @Column(name = "amount")
    Long amount;

    @Column(name = "give_time")
    Timestamp giveTime;
}
