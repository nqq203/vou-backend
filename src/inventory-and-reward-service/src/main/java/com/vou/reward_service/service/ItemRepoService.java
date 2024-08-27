package com.vou.reward_service.service;

import com.vou.reward_service.model.ItemRepo;
import com.vou.reward_service.model.Voucher;
import com.vou.reward_service.repository.ItemRepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemRepoService {
    @Autowired
    private ItemRepoRepository itemRepoRepository;

    boolean checkIfUserHasItemsForVoucher(Voucher voucher, Long userId) throws Exception {
        List<Long> itemIds = new ArrayList<>();

        Optional.ofNullable(voucher.getIdItem1()).ifPresent(action -> itemIds.add(voucher.getIdItem1()));
        Optional.ofNullable(voucher.getIdItem2()).ifPresent(action -> itemIds.add(voucher.getIdItem2()));
        Optional.ofNullable(voucher.getIdItem3()).ifPresent(action -> itemIds.add(voucher.getIdItem3()));
        Optional.ofNullable(voucher.getIdItem4()).ifPresent(action -> itemIds.add(voucher.getIdItem4()));

        for (Long itemId : itemIds) {
            if (!itemRepoRepository.existsItemRepoByIdItemAndAmountIsGreaterThan(itemId, (long) 0)) {
                throw new Exception("Not have enough items to exchange for this voucher");
            }
        }


        if (voucher.getIdItem5() != null) {
            Boolean enoughCoin = itemRepoRepository.existsItemRepoByIdItemAndAmountIsGreaterThan(voucher.getIdItem5(), voucher.getAimCoin());
            if (enoughCoin) {
                return true;
            } else {
                throw new Exception("Not have enough coins to exchange for this voucher");
            }
        } else {
            return true;
        }
    }

    boolean exchangeItemForVoucher(Voucher voucher) {
        List<Long> itemIds = new ArrayList<>();

        Optional.ofNullable(voucher.getIdItem1()).ifPresent(action -> itemIds.add(voucher.getIdItem1()));
        Optional.ofNullable(voucher.getIdItem2()).ifPresent(action -> itemIds.add(voucher.getIdItem2()));
        Optional.ofNullable(voucher.getIdItem3()).ifPresent(action -> itemIds.add(voucher.getIdItem3()));
        Optional.ofNullable(voucher.getIdItem4()).ifPresent(action -> itemIds.add(voucher.getIdItem4()));

        for (Long itemId : itemIds) {
            ItemRepo itemRepo = itemRepoRepository.findItemRepoByIdItem(itemId);
            Long remainItem = itemRepo.getAmount() - 1;
            itemRepo.setAmount(remainItem);
            itemRepoRepository.save(itemRepo);
        }


        if (voucher.getIdItem5() != null) {
            ItemRepo itemRepo = itemRepoRepository.findItemRepoByIdItem(voucher.getIdItem5());
            itemRepo.setAmount(itemRepo.getAmount() - voucher.getAimCoin());
            itemRepoRepository.save(itemRepo);
            return true;
        } else {
            return true;
        }
    }
}
