package com.vou.streaming_service.service;

import com.vou.streaming_service.model.Game;
import com.vou.streaming_service.model.PlaySession;
import com.vou.streaming_service.repository.GameRepository;
import com.vou.streaming_service.repository.PlaySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaySessionService {
    @Autowired
    PlaySessionRepository playSessionRepository;

    @Autowired
    GameRepository gameRepository;

    public PlaySession findOrCreatePlaySession(Long idGame, Long idPlayer) {
        PlaySession playSession = playSessionRepository.findPlaySessionByIdPlayerAndIdGame(idPlayer, idGame);
        if (playSession != null) {
            return playSession;
        }
        PlaySession newPlaySession = new PlaySession();
        newPlaySession.setIdGame(idGame);
        newPlaySession.setIdPlayer(idPlayer);
        newPlaySession.setTurns(10);
        newPlaySession.setScore(0);
        return playSessionRepository.save(newPlaySession);
    }

    public PlaySession findPlaySessionByIdGameAndIdPlayer(Long idGame, Long idPlayer) {
        return playSessionRepository.findPlaySessionByIdPlayerAndIdGame(idPlayer, idGame);
    }

    public void shareToGetTurns(Long idGame, Long idPlayer) {
        playSessionRepository.increaseTurns(idPlayer, idGame, 1);
    }


    public int countParticipantsByIdEvent(Long idEvent) {
        Game game = gameRepository.findByIdEvent(idEvent);
        return playSessionRepository.countDistinctByIdGame(game.getIdEvent());
    }
}
