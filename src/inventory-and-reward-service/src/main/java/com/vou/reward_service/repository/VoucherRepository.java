package com.vou.reward_service.repository;

import com.vou.reward_service.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {
    Voucher findByCode(String code);
    Voucher findByIdEvent(Long idEvent);
    Boolean existsByCode(String code);
}