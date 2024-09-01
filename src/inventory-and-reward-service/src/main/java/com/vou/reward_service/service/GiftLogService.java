package com.vou.reward_service.service;

import com.vou.reward_service.common.ApiResponse;
import com.vou.reward_service.common.NotFoundException;
import com.vou.reward_service.model.GiftLog;
import com.vou.reward_service.repository.GiftLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GiftLogService {
    @Autowired
    private GiftLogRepository giftLogRepository;

    @Autowired
    private ItemRepoService itemRepoService;

    @Autowired
    private UserService userService;

    public void giftAnItemForUser(Long itemId, Long receiverId, Long senderId, String receiverName, String senderName, Long amount) throws Exception {
        // Check user có đủ item ko
        System.out.println(itemRepoService.checkIfUserHaveAmountOfItemLargerThan(senderId, amount, itemId));
        if (!itemRepoService.checkIfUserHaveAmountOfItemLargerThan(senderId, amount, itemId)) {
            throw new Exception("Người dùng không đủ item để tặng");
        }
        // Check receiver có tồn tại hay ko
        LinkedHashMap<String, Object> response = userService.findPlayerById(receiverId);
        for (Map.Entry<String, Object> entry : response.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            System.out.println("Key: " + key + ", Value: " + value);
        }
        System.out.println(response.get("code") != (Integer) 200);

        if (!response.get("code").equals((Integer) 200)) {
            throw new Exception("Nguoi nhan khong ton tai");
        }
        // Tiến hành trừ item của sender
        itemRepoService.updateItemAmountOfUser(itemId, senderId, -amount);
        // Tiến hành cộng item của receiver
        itemRepoService.updateItemAmountOfUser(itemId, receiverId, amount);
        // Trừ thành công thì tạo gift log, nếu trừ khong thanh cong thi exception da duoc throw
        GiftLog newGiftLog = new GiftLog();
        newGiftLog.setIdItem(itemId);
        System.out.println(itemId);
        newGiftLog.setAmount(amount);
        newGiftLog.setSenderName(senderName);
        newGiftLog.setReceiverName(receiverName);
        newGiftLog.setUidReceiver(receiverId);
        newGiftLog.setUidSender(senderId);
        giftLogRepository.save(newGiftLog);
    }

    public HashMap<String, Object> getGiftingHistoryByUserId(Long id_user) {
        List<GiftLog> giftLogsSent = giftLogRepository.findGiftLogsByUidSender(id_user);
        List<GiftLog> giftLogsReceive = giftLogRepository.findGiftLogsByUidReceiver(id_user);
        HashMap<String, Object> result = new HashMap<>();
        result.put("sendHistory", giftLogsSent);
        result.put("receiveHistory", giftLogsReceive);
        return result;
    }
}
