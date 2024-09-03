package com.vou.reward_service.service;

import com.vou.reward_service.common.NotFoundException;
import com.vou.reward_service.model.Item;
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
            if (!itemRepoRepository.existsItemRepoByIdItemAndIdPlayerAndAmountGreaterThanEqual(itemId, userId, (long) 0)) {
                throw new Exception("Not have enough items to exchange for this voucher");
            }
        }


        if (voucher.getIdItem5() != null) {
            Boolean enoughCoin = itemRepoRepository.existsItemRepoByIdItemAndIdPlayerAndAmountGreaterThanEqual(voucher.getIdItem5(), userId, voucher.getAimCoin());
            if (enoughCoin) {
                return true;
            } else {
                throw new Exception("Not have enough coins to exchange for this voucher");
            }
        } else {
            return true;
        }
    }

    boolean exchangeItemForVoucher(Voucher voucher, Long idPlayer) {
        List<Long> itemIds = new ArrayList<>();

        Optional.ofNullable(voucher.getIdItem1()).ifPresent(action -> itemIds.add(voucher.getIdItem1()));
        Optional.ofNullable(voucher.getIdItem2()).ifPresent(action -> itemIds.add(voucher.getIdItem2()));
        Optional.ofNullable(voucher.getIdItem3()).ifPresent(action -> itemIds.add(voucher.getIdItem3()));
        Optional.ofNullable(voucher.getIdItem4()).ifPresent(action -> itemIds.add(voucher.getIdItem4()));

        for (Long itemId : itemIds) {
            ItemRepo itemRepo = itemRepoRepository.findItemRepoByIdItemAndIdPlayer(itemId, idPlayer);
            Long remainItem = itemRepo.getAmount() - 1;
            itemRepo.setAmount(remainItem);
            itemRepoRepository.save(itemRepo);
        }


        if (voucher.getIdItem5() != null) {
            ItemRepo itemRepo = itemRepoRepository.findItemRepoByIdItemAndIdPlayer(voucher.getIdItem5(), idPlayer);
            itemRepo.setAmount(itemRepo.getAmount() - voucher.getAimCoin());
            itemRepoRepository.save(itemRepo);
            return true;
        } else {
            return true;
        }
    }

    public List<ItemRepo> getItemRepoListByIdUser(Long idUser) throws Exception {
        try {
            return itemRepoRepository.findItemReposByIdPlayer(idUser);
        } catch (Exception e) {
            throw new Exception("Error in getItemRepoListByIdUser: " + e.getMessage());
        }
    }

    public int incrementAmountByIdItemRepo(Long idItemRepo) {
        return itemRepoRepository.incrementAmountByIdItemRepo(idItemRepo);
    }

    public int incrementAmountCoinByIdItemRepo(Long idItemRepo, Long updatedAmount) {
        return itemRepoRepository.incrementAmountCoinByIdItemRepo(idItemRepo, updatedAmount);
    }

    public void updateItemAmountOfUser(Long idItem, Long userId, Long amount) throws Exception {
        ItemRepo itemRepo = itemRepoRepository.findItemRepoByIdItemAndIdPlayer(idItem, userId);
        if (amount < 0) {
            if (itemRepo == null)
                throw new NotFoundException("User khong so huu item nay");
            if (itemRepo.getAmount() + amount < 0) {
                throw new Exception("Item sau khi update khong duoc nho hon 0");
            }
        } else {
            if (itemRepo == null) {
                itemRepo = new ItemRepo();
                itemRepo.setAmount(0L);
                itemRepo.setIdItem(idItem);
                itemRepo.setIdPlayer(userId);
            }
        }

        itemRepo.setAmount(itemRepo.getAmount() + amount);
        itemRepoRepository.save(itemRepo);
    }

    public ItemRepo getItemRepoByIdItemRepo(Long idItemRepo) throws Exception {
        try {
            return itemRepoRepository.findItemRepoByIdItemRepo(idItemRepo);
        } catch (Exception e) {
            throw new Exception("Error in getItemRepoByIdItemRepo service: " + e.getMessage());
        }
    }

    public List<ItemRepo> createItemRepos(Long userId, List<Item> itemList) throws Exception {
        try {
            System.out.println("In createItemRepos, userId: " + userId);
            System.out.println("In createItemRepos, itemList: " + itemList);
            List<ItemRepo> itemRepoList = new ArrayList<>();
            itemList.forEach(item -> {
                ItemRepo itemRepo = new ItemRepo();
                itemRepo.setIdPlayer(userId);
                itemRepo.setIdItem(item.getIdItem());
                itemRepo.setAmount(Long.valueOf(0));
                ItemRepo savedRepo = itemRepoRepository.save(itemRepo);
                itemRepoList.add(savedRepo);
            });
            System.out.println("Item Repo created: " + itemRepoList);
            return itemRepoList;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean checkIfUserHaveAmountOfItemLargerThan(Long userId, Long amount, Long idItem) {
        return itemRepoRepository.existsItemRepoByIdItemAndIdPlayerAndAmountGreaterThanEqual(idItem, userId, amount);
    }
}
