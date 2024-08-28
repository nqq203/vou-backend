/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 08/08/2024 - 10:39
 */
package com.vou.streaming_service.controller;

import com.vou.streaming_service.dto.GameInfoDTO;
import com.vou.streaming_service.dto.QuizDTO;
import com.vou.streaming_service.libs.RedisCache;
import com.vou.streaming_service.model.Game;
import com.vou.streaming_service.model.Quiz;
import com.vou.streaming_service.model.QuizGame;
import com.vou.streaming_service.model.ShakeGame;
import com.vou.streaming_service.repository.GameRepository;
import com.vou.streaming_service.repository.QuizGameRepository;
import com.vou.streaming_service.repository.QuizRepository;
import com.vou.streaming_service.repository.ShakeGameRepository;
import com.vou.streaming_service.service.EventSchedulerService;
import com.vou.streaming_service.service.MessageService;
import com.vou.streaming_service.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class MessageController{

    private final MessageService messageService;
    private RedisCache redisCache;
    private EventSchedulerService eventSchedulerService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private QuizGameRepository quizGameRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ShakeGameRepository shakeGameRepository;

    @GetMapping("message/{room}")
    public ResponseEntity<List<String>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getPlayers(room));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createGame(){

        String date = messageService.startGame(1231123L);
        return ResponseEntity.ok(date);
    }

    @PostMapping("/quiz/create")
    public ResponseEntity<String> createQuiz(@RequestBody GameInfoDTO gameInfoDTO){
        List<QuizDTO> quizzdto = gameInfoDTO.getQuiz();
        Game game = new Game(gameInfoDTO.getName(),gameInfoDTO.getGameType(), gameInfoDTO.getEventId());
        gameRepository.save(game);
        if (gameInfoDTO.getGameType().equals("shake-game")){
            ShakeGame shakeGame = new ShakeGame();
            shakeGame.setGame(game);
            shakeGameRepository.save(shakeGame);
            return ResponseEntity.ok("Save successfully");

        }
        QuizGame quizGame = new QuizGame(4);
        quizGame.setGame(game);
        quizGameRepository.save(quizGame);

        List<Quiz> quizzes = quizzdto.stream().map(quizz-> new Quiz(quizz, game.getIdGame())).collect(Collectors.toList());
        quizService.saveQuizzes(quizzes);
        return ResponseEntity.ok("Save successfully");
    }
    @GetMapping("/game-info")
    public ResponseEntity<GameInfoDTO>  getDetailGameInfo(@RequestParam Long eventId){
        Game game = gameRepository.findByIdEvent(eventId);

        List<Quiz> quizzes= quizRepository.findAllByIdGame(game.getIdGame());

        List<QuizDTO> quizDto= quizzes.stream()
                .map(QuizDTO::new)
                .collect(Collectors.toList());
        GameInfoDTO gameInfoDTO =new GameInfoDTO(
                game.getIdGame(),
                game.getName(),
                game.getType(),
                game.getStartedAt(),
                game.getIdEvent(),
                quizDto
        );
        return ResponseEntity.ok(gameInfoDTO);
    }

}


