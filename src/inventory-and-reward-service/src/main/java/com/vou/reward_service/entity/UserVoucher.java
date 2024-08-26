package com.vou.reward_service.entity;

import com.vou.reward_service.model.Voucher;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserVoucher {
    private Voucher voucher;
    private Long amount;

    public UserVoucher(Voucher v, Long amount) {
        this.voucher = v;
        this.amount = amount;
    }
}
