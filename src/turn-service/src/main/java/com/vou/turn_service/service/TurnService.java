package com.vou.turn_service.service;

import com.vou.turn_service.model.PlaySession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnService {
    @Autowired
    GameClient gameClient;

    public PlaySession createPlaySession(Long idPlayer, Long idGame, int score, int turns) {
        try {
            PlaySession playSession = new PlaySession(idPlayer, idGame, score, turns);
            return gameClient.createPlaySession(playSession);
        } catch (Exception e) {
            System.err.println("Internal server error when try to create new play session: " + e.getMessage());
            return null;
        }
    }

    public byte checkExistPlaySession(Long idPlayer, Long idGame, Long idPlaySession) {
        try {
            if (idPlaySession != null) {
                return gameClient.checkExistPlaySession(null, null, idPlaySession);
            } else {
                return gameClient.checkExistPlaySession(idPlayer, idGame, null);
            }
        } catch (Exception e) {
            System.err.println("Error when try to get play session: " + e.getMessage());
            return 3;
        }
    }


    public PlaySession getPlaySession(Long idPlayer, Long idGame) {
        try {
            return gameClient.getPlaySessionByIdPlayerAndIdGame(idPlayer, idGame);
        } catch (Exception e) {
            System.err.println("Error retrieving play session in turn service: " + e.getMessage());
            return null;
        }
    }

    public PlaySession updatePlaySession(PlaySession playSession) {
        try {
            return gameClient.updatePlaySession(playSession);
        }
        catch (Exception e) {
            System.err.println("Error updating play session in turn service: " + e.getMessage());
            return null;
        }
    }
}
