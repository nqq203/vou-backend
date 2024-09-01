package com.vou.reward_service.repository;

import com.vou.reward_service.model.GiftLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftLogRepository extends JpaRepository<GiftLog, Long> {
    List<GiftLog> findGiftLogsByUidSender(Long senderId);
    List<GiftLog> findGiftLogsByUidReceiver(Long receiverId);
}
