/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 08/08/2024 - 10:39
 */
package com.vou.streaming_service.controller;

import com.vou.streaming_service.dto.GameInfoDTO;
import com.vou.streaming_service.dto.QuizDTO;
import com.vou.streaming_service.libs.RedisCache;
import com.vou.streaming_service.model.*;
import com.vou.streaming_service.repository.GameRepository;
import com.vou.streaming_service.repository.ItemRepoRepository;
import com.vou.streaming_service.repository.QuizGameRepository;
import com.vou.streaming_service.repository.ShakeGameRepository;
import com.vou.streaming_service.service.EventSchedulerService;
import com.vou.streaming_service.service.ItemService;
import com.vou.streaming_service.service.MessageService;
import com.vou.streaming_service.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
    private ItemService itemService;

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ItemRepoRepository itemRepoRepository;

    @Autowired
    private QuizGameRepository quizGameRepository;

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


    @GetMapping("/shake/{id_event}/{id_player}")
    public ResponseEntity<String> playShakeGame(@PathVariable Long id_event, @PathVariable Long id_player) throws Exception {
        try {
            ItemRepo[] items = itemService.getItem(id_event);

            double winProbability = 0.10;

            // Generate a random number between 0 and 1
            Random random = new Random();
            double randomValue = random.nextDouble();

            if (randomValue < winProbability) {
                int itemIndex = random.nextInt(items.length);
                ItemRepo wonItem = items[itemIndex];

                ItemRepo existItem = itemRepoRepository.findItembyIdUser(id_player, wonItem.getId_item());
                if (existItem.getId_item() != null) {
                    itemService.updateQuantityItem(existItem.getId_itemRepo());
                } else {
                    ItemRepo newItemRepo = new ItemRepo(id_player, wonItem.getId_item(), 1);
                    itemRepoRepository.save(newItemRepo);
                }
                return ResponseEntity.ok("Congratulations! You've won: " + wonItem);

            } else {
                return ResponseEntity.ok("Sorry! You didn't win anything this time.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}


