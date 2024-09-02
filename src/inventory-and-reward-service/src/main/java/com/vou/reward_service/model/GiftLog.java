package com.vou.reward_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "giftlog")
public class GiftLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_giftlog")
    Long idGiftLog;

    @Column(name = "uid_receiver")
    Long uidReceiver;

    @Column(name = "uid_sender")
    Long uidSender;

    @Column(name = "sender_name")
    String senderName;

    @Column(name = "receiver_name")
    String receiverName;

    @Column(name = "id_item")
    Long idItem;

    @Column(name = "amount")
    Long amount;

    @CreationTimestamp
    @Column(name = "give_time")
    Timestamp giveTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item", referencedColumnName = "id_item", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    Item item;
}
