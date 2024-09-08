package com.vou.statistics_service.repository;

import com.vou.statistics_service.model.QuizWinner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizWinnerRepository extends JpaRepository<QuizWinner, Long> {
    QuizWinner findByIdEventAndIdGame(Long idEvent, Long idGame);
    List<QuizWinner> findQuizWinnersByIdEventAndIdGame(Long idEvent, Long idGame);
}
