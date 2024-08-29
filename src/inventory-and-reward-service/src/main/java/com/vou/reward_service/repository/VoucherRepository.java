package com.vou.reward_service.repository;

import com.vou.reward_service.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Voucher findByCode(String code);
    Voucher findByIdEvent(Long idEvent);
    List<Voucher> findVouchersByIdEvent(Long idEvent);
}