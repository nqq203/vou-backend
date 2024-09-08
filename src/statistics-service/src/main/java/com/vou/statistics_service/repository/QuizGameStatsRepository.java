package com.vou.statistics_service.repository;

import com.vou.statistics_service.model.QuizGameStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizGameStatsRepository extends JpaRepository<QuizGameStats, Long> {
    QuizGameStats findByIdEventAndIdGame(Long idEvent, Long idGame);
}
