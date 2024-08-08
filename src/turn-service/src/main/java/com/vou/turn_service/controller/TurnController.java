package com.vou.turn_service.controller;

import com.vou.turn_service.entity.CreateTurnRequest;
import com.vou.turn_service.service.TurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/turn")
@CrossOrigin
public class TurnController {
    private final TurnService turnService;

    @Autowired
    public TurnController(TurnService turnService) {
        this.turnService = turnService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createTurn(@RequestBody CreateTurnRequest createTurnRequest) {
        return null;
    }

    @GetMapping("/")
    public ResponseEntity<?> getListTurns() {
        return null;
    }

    @GetMapping("/{idPlaySession}")
    public ResponseEntity<?> getPlaySessionById(@PathVariable Long idPlaySession) {
        return null;
    }

    @GetMapping("/{idPlayer}/{idGame}")
    public ResponseEntity<?> getTurn(@PathVariable Long idPlayer, Long idGame) {
        return null;
    }

    @PutMapping("/{idPlaySession}")
    public ResponseEntity<?> updatePlaySession(@PathVariable Long idPlaySession) {
       return null;
    }

    @PutMapping("/{idPlayer}/{idGame}")
    public ResponseEntity<?> updateTurn(@PathVariable Long idPlayer, Long idGame) {
        return null;
    }

    @DeleteMapping("/{idPlaySession}")
    public ResponseEntity<?> deletePlaySession(@PathVariable Long idPlaySession) {
        return null;
    }

    @DeleteMapping("/{idPlayer}/{idGame}")
    public ResponseEntity<?> deleteTurn(@PathVariable Long idPlayer, Long idGame) {
        return null;
    }
}
