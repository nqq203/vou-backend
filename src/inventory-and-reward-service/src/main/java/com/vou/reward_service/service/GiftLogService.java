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

    public void giftAnItemForUser(Long itemId, Long receiverId, Long senderId, Long amount) throws Exception {
        if (Objects.equals(senderId, receiverId))
            throw new Exception("Không thể tự gửi vật phẩm cho chính mình");

        LinkedHashMap<String, Object> senderResponse = userService.findPlayerById(senderId);

        // Check user có đủ item ko
        if (!itemRepoService.checkIfUserHaveAmountOfItemLargerThan(senderId, amount, itemId)) {
            throw new Exception("Người dùng không đủ item để tặng");
        }
        // Check receiver có tồn tại hay ko
        LinkedHashMap<String, Object> receiverResponse = userService.findPlayerById(receiverId);
        // Tiến hành trừ item của sender
        itemRepoService.updateItemAmountOfUser(itemId, senderId, -amount);
        // Tiến hành cộng item của receiver
        itemRepoService.updateItemAmountOfUser(itemId, receiverId, amount);
        // Trừ thành công thì tạo gift log, nếu trừ khong thanh cong thi exception da duoc throw
        GiftLog newGiftLog = new GiftLog();
        newGiftLog.setIdItem(itemId);
        System.out.println(itemId);
        newGiftLog.setAmount(amount);
        newGiftLog.setSenderName((String) senderResponse.get("fullName"));
        newGiftLog.setReceiverName((String) receiverResponse.get("fullName"));
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
