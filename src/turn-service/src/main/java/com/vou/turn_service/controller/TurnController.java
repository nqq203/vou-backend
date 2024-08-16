package com.vou.turn_service.controller;

import com.vou.turn_service.common.BadRequest;
import com.vou.turn_service.common.CreatedResponse;
import com.vou.turn_service.common.InternalServerError;
import com.vou.turn_service.common.SuccessResponse;
import com.vou.turn_service.entity.PlaySessionRequest;
import com.vou.turn_service.model.PlaySession;
import com.vou.turn_service.service.TurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // A Play session will be created when users join an event (shake game or quiz game)
    @PostMapping("/")
    public ResponseEntity<?> createPlaySession(@RequestBody PlaySessionRequest playSessionRequest) {
        if (playSessionRequest == null) {
            return ResponseEntity.badRequest().body(new BadRequest("Wrong request body format"));
        }
        byte existPlaySession = turnService.checkExistPlaySession(playSessionRequest.getIdPlayer(), playSessionRequest.getIdGame(), null);
        if (existPlaySession == 0) {
            return ResponseEntity.badRequest().body(new BadRequest("Exist play session"));
        } else if (existPlaySession == 2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(
                    "Internal server error: Error checking exist play session in game client"
            ));
        } else if (existPlaySession == 3) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(
                    "Internal server error: Error checking exist play session in turn service"
            ));
        }
        PlaySession newPlaySession = turnService.createPlaySession(
                playSessionRequest.getIdPlayer(),
                playSessionRequest.getIdGame(),
                playSessionRequest.getScore(),
                playSessionRequest.getTurns());
        if (newPlaySession == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(
                    "Internal server error: Error creating session"
            ));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreatedResponse("Create new play session successfully", newPlaySession));
    }

//    @GetMapping("/")
//    public ResponseEntity<?> getListTurns() {
//        return null;
//    }

    @GetMapping("/{idPlaySession}")
    public ResponseEntity<?> getPlaySessionById(@PathVariable Long idPlaySession) {
        return null;
    }

    @GetMapping("/{idPlayer}/{idGame}")
    public ResponseEntity<?> getPlaySession(@PathVariable Long idPlayer, @PathVariable Long idGame) {
        byte isExisted = turnService.checkExistPlaySession(idPlayer, idGame, null);
        if (isExisted == 0) {
            return ResponseEntity.badRequest().body(new BadRequest("Exist play session"));
        } else if (isExisted == 2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(
                    "Internal server error: Error checking exist play session in game client"
            ));
        } else if (isExisted == 3) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(
                    "Internal server error: Error checking exist play session in turn service"
            ));
        }

        PlaySession playSession = turnService.getPlaySession(idPlayer, idGame);
        if (playSession == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Error retrieving play session"));
        }
        return ResponseEntity.ok(new SuccessResponse(
                "Get play session successfully",
                HttpStatus.OK,
                playSession
        ));
    }

    @PutMapping("/{idPlaySession}")
    public ResponseEntity<?> updatePlaySessionById(@PathVariable Long idPlaySession, @RequestBody PlaySessionRequest playSessionRequest) {
        if (playSessionRequest == null) {
            return ResponseEntity.badRequest().body(new BadRequest("Wrong request body format"));
        }
        byte isExisted = turnService.checkExistPlaySession(null, null, idPlaySession);
        if (isExisted == 0) {
            return ResponseEntity.badRequest().body(new BadRequest("Exist play session"));
        } else if (isExisted == 2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(
                    "Internal server error: Error checking exist play session in game client"
            ));
        } else if (isExisted == 3) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError(
                    "Internal server error: Error checking exist play session in turn service"
            ));
        }

        PlaySession playSession = new PlaySession(
                playSessionRequest.getIdPlayer(),
                playSessionRequest.getIdGame(),
                playSessionRequest.getScore(),
                playSessionRequest.getTurns());
        PlaySession updatedPlaySession = turnService.updatePlaySession(playSession);
        if (updatedPlaySession == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Error updating play session"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse("Update play session successfully", updatedPlaySession));
    }

//    @PutMapping("/{idPlayer}/{idGame}")
//    public ResponseEntity<?> updateTurn(@PathVariable Long idPlayer,@PathVariable Long idGame) {
//        return null;
//    }

    @DeleteMapping("/{idPlaySession}")
    public ResponseEntity<?> deletePlaySession(@PathVariable Long idPlaySession) {
        return null;
    }

    @DeleteMapping("/{idPlayer}/{idGame}")
    public ResponseEntity<?> deleteTurn(@PathVariable Long idPlayer,@PathVariable Long idGame) {
        return null;
    }
}
