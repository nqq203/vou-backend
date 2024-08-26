package com.vou.reward_service.repository;

import com.vou.reward_service.entity.UserVoucher;
import com.vou.reward_service.model.VoucherRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoucherRepoRepository extends JpaRepository<VoucherRepo, Long> {
    @Query("SELECT new com.vou.reward_service.entity.UserVoucher(V, VP.amount) " +
            "FROM Voucher V JOIN VoucherRepo VP ON V.code = VP.codeVoucher " +
            "WHERE VP.idPlayer = :userId")
    List<UserVoucher> findVouchersByUserId(@Param("userId") Long userId);

    VoucherRepo findVoucherRepoByCodeVoucher(String codeVoucher);

    VoucherRepo findVoucherRepoByIdPlayerAndCodeVoucher(Long idPlayer, String code);

    List<VoucherRepo> findVoucherReposByIdPlayer(Long idPlayer);
}
