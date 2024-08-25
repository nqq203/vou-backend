package com.vou.streaming_service.repository;

import com.vou.streaming_service.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}

