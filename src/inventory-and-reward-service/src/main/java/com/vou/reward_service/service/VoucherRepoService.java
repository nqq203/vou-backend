package com.vou.reward_service.service;

import com.vou.reward_service.common.NotFoundException;
import com.vou.reward_service.model.Voucher;
import com.vou.reward_service.model.VoucherRepo;
import com.vou.reward_service.repository.VoucherRepoRepository;
import com.vou.reward_service.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherRepoService {
    @Autowired
    private VoucherRepoRepository voucherRepoRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ItemRepoService itemRepoService;
    @Autowired
    private EventService eventService;

    public boolean exchangeItem(String code, Long id) throws Exception {
        Voucher voucher = voucherRepository.findByCode(code.toUpperCase());

        if (voucher == null) {
            throw new NotFoundException("Voucher not found");
        }

        if (itemRepoService.checkIfUserHasItemsForVoucher(voucher, id)) {
            VoucherRepo existingVoucher = voucherRepoRepository.findVoucherRepoByIdPlayerAndCodeVoucher(id, voucher.getCode().toUpperCase());
            if (existingVoucher != null) {
                existingVoucher.setAmount(existingVoucher.getAmount() + 1);
                itemRepoService.exchangeItemForVoucher(voucher, id);
                voucherRepoRepository.save(existingVoucher);
                eventService.decreaseRemainingVoucher(voucher.getIdEvent());
                return true;
            } else {
                VoucherRepo newRepo = new VoucherRepo();
                newRepo.setIdPlayer(id);
                newRepo.setCodeVoucher(code.toUpperCase());
                newRepo.setAmount((long) 1);
                itemRepoService.exchangeItemForVoucher(voucher, id);
                voucherRepoRepository.save(newRepo);
                eventService.decreaseRemainingVoucher(voucher.getIdEvent());
                return true;
            }
        }
        throw new Exception("Not enough required items to exchange for this voucher");
    }

    public boolean rewardVoucherQuizGame(List<Long> winnerIds, String voucherRewardCode) {
        Voucher voucher = voucherRepository.findByCode(voucherRewardCode.toUpperCase());

        if (voucher == null) {
            throw new NotFoundException("Voucher not found");
        }

        for(Long winnerId : winnerIds) {
            VoucherRepo existingVoucher = voucherRepoRepository.findVoucherRepoByIdPlayerAndCodeVoucher(winnerId, voucher.getCode().toUpperCase());
            if (existingVoucher != null) {
                existingVoucher.setAmount(existingVoucher.getAmount() + 1);
                voucherRepoRepository.save(existingVoucher);
            } else {
                VoucherRepo newRepo = new VoucherRepo();
                newRepo.setIdPlayer(winnerId);
                newRepo.setCodeVoucher(voucherRewardCode.toUpperCase());
                newRepo.setAmount((long) 1);
                voucherRepoRepository.save(newRepo);
            }
        }
        return true;
    }
}
