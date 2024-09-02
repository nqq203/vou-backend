package com.vou.streaming_service.repository;

import com.vou.streaming_service.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findAllByIdGame(Long idGame);
    Optional<Quiz> findByIdGameAndQuestion(Long eventId, String question);
}
