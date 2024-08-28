package com.vou.reward_service.service;

import com.vou.reward_service.common.NotFoundException;
import com.vou.reward_service.model.Voucher;
import com.vou.reward_service.model.VoucherRepo;
import com.vou.reward_service.repository.VoucherRepoRepository;
import com.vou.reward_service.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoucherRepoService {
    @Autowired
    private VoucherRepoRepository voucherRepoRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ItemRepoService itemRepoService;

    public boolean exchangeItem(String code, Long id) throws Exception {
        Voucher voucher = voucherRepository.findByCode(code.toUpperCase());

        if (voucher == null) {
            throw new NotFoundException("Voucher not found");
        }

        if (itemRepoService.checkIfUserHasItemsForVoucher(voucher, id)) {
            VoucherRepo existingVoucher = voucherRepoRepository.findVoucherRepoByIdPlayerAndCodeVoucher(id, voucher.getCode().toUpperCase());
            if (existingVoucher != null) {
                existingVoucher.setAmount(existingVoucher.getAmount() + 1);
                voucherRepoRepository.save(existingVoucher);
                itemRepoService.exchangeItemForVoucher(voucher);
                return true;
            } else {
                VoucherRepo newRepo = new VoucherRepo();
                newRepo.setIdPlayer(id);
                newRepo.setCodeVoucher(code.toUpperCase());
                newRepo.setAmount((long) 1);
                itemRepoService.exchangeItemForVoucher(voucher);

                voucherRepoRepository.save(newRepo);
                return true;
            }
        }
        throw new Exception("Not enough required items to exchange for this voucher");
    }
}
