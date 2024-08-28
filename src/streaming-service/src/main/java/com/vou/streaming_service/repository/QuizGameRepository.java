package com.vou.streaming_service.repository;

import com.vou.streaming_service.model.QuizGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizGameRepository extends JpaRepository<QuizGame, Long> {
}
