package com.vou.reward_service.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class GiftItemRequest {
    private Long receiverId;
    private String receiverUsername;
    private Long senderId;
    private String senderUsername;
    private Long itemId;
    private Long amount;
    private Timestamp giftTime;
}
