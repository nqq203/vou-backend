package com.vou.streaming_service.repository;

import com.vou.streaming_service.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByIdEvent(Long idEvent);
}

