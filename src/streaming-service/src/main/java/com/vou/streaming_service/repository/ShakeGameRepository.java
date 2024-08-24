package com.vou.streaming_service.repository;

import com.vou.streaming_service.model.ShakeGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShakeGameRepository extends JpaRepository<ShakeGame, Long> {
}
