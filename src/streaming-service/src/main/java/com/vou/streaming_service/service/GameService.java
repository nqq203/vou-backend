package com.vou.streaming_service.service;

import com.vou.streaming_service.model.Game;
import com.vou.streaming_service.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public Game findGameByIdGame(Long idGame) {
        try {
            return gameRepository.findByIdGame(idGame);
        } catch (Exception e) {
            return null;
        }
    }
}
