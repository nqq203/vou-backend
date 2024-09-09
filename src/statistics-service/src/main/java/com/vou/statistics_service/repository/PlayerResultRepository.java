package com.vou.statistics_service.repository;

import com.vou.statistics_service.model.PlayerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerResultRepository extends JpaRepository<PlayerResult, Long> {
    //PlayerResult findByIdEvent(Long idEvent);
    List<PlayerResult> findPlayerResultByIdEventAndRankIsBetween(Long idEvent, Integer rank, Integer rank2);
}
