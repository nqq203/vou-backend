package com.vou.streaming_service.repository;

import com.vou.streaming_service.model.PlaySession;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PlaySessionRepository extends JpaRepository<PlaySession, Long> {
    PlaySession findPlaySessionByIdPlayerAndIdGame(Long idPlayer, Long idGame);
    @Transactional
    @Modifying
    @Query("UPDATE PlaySession ps SET ps.turns = ps.turns - 1 WHERE ps.idPlayer = :idPlayer AND ps.idGame = :idGame AND ps.turns > 0")
    void decrementTurns(Long idPlayer, Long idGame);
}
