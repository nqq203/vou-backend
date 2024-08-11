package com.vou.reward_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "voucherrepo")
public class VoucherRepo {
    @Id
    @Column(name = "id_voucherrepo")
    Long idVoucherRepo;

    @Column(name = "id_player")
    Long idPlayer;

    @Column(name = "code_voucher")
    String codeVoucher;

    @Column(name = "amount")
    Long amount;
}
