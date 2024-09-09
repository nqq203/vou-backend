package com.vou.statistics_service.repository;

import com.vou.statistics_service.model.QuizQuestionStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionStatsRepository extends JpaRepository<QuizQuestionStats, Long> {
//    QuizQuestionStats findByIdQuizQuestion(Long quizQuestionId);
//
//    List<QuizQuestionStats> findQuizQuestionStatsByIdEventAndIdGame(Long idEvent, Long idGame);
}
