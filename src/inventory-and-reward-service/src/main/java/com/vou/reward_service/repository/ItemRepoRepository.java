package com.vou.reward_service.repository;

import com.vou.reward_service.model.ItemRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepoRepository extends JpaRepository<ItemRepo, Long> {
    boolean existsItemRepoByIdItemAndIdPlayerAndAmountIsLessThanEqual(Long idItem, Long idPlayer, Long amount);

    boolean existsItemRepoByIdItemAndIdPlayerAndAmountGreaterThanEqual(Long idItem, Long idPlayer, Long amount);

    ItemRepo findItemRepoByIdItemAndIdPlayer(Long idItem, Long idPlayer);

    List<ItemRepo> findItemReposByIdPlayer(Long idPlayer);

    @Transactional
    @Modifying
    @Query("UPDATE ItemRepo ir SET ir.amount = ir.amount + 1 WHERE ir.idItemRepo = :idItemRepo")
    int incrementAmountByIdItemRepo(Long idItemRepo);

    ItemRepo findItemRepoByIdItemRepo(Long idItemRepo);

    @Transactional
    @Modifying
    @Query("UPDATE ItemRepo ir SET ir.amount = ir.amount + :updatedAmount WHERE ir.idItemRepo = :idItemRepo")
    int incrementAmountCoinByIdItemRepo(Long idItemRepo, Long updatedAmount);
}
