/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 08/08/2024 - 10:39
 */
package com.vou.streaming_service.controller;

import com.vou.streaming_service.client.EventClient;
import com.vou.streaming_service.common.*;
import com.vou.streaming_service.dto.*;
import com.vou.streaming_service.libs.RedisCache;
import com.vou.streaming_service.model.*;
import com.vou.streaming_service.repository.*;
import com.vou.streaming_service.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
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
    private PlaySessionRepository playSessionRepository;

    @Autowired
    private QuizGameRepository quizGameRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private ShakeGameRepository shakeGameRepository;

    @Autowired
    private RewardService rewardService;

    @Autowired
    private PlaySessionService playSessionService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private EventClient eventClient;

    @GetMapping("message/{room}")
    public ResponseEntity<List<String>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getPlayers(room));
    }

//    @PostMapping("/create")
//    public ResponseEntity<String> createGame(){
//        List<QuizDTO> list = new ArrayList<>();
//        Timestamp time = Timestamp.valueOf("2024-09-04 01:20:30.123");
//        String date = messageService.startGame( list, 12312L, time);
//        return ResponseEntity.ok(date);
//    }

    @PostMapping("/quiz/create")
    public ResponseEntity<String> createQuiz(@RequestBody GameInfoDTO gameInfoDTO){
        System.out.println("gameInfoDTO" +  gameInfoDTO);
        List<QuizDTO> quizzdto = gameInfoDTO.getQuiz();

        Game game = new Game(gameInfoDTO.getName(),gameInfoDTO.getGameType(), gameInfoDTO.getEventId());
        System.out.println(game);
        gameRepository.save(game);
        if (gameInfoDTO.getGameType().equals("shake-game")){
            ShakeGame shakeGame = new ShakeGame();
            shakeGame.setGame(game);
            shakeGameRepository.save(shakeGame);
            return ResponseEntity.ok("Save successfully");
        }

        game.setStartedAt(gameInfoDTO.getStartedAt());
        QuizGame quizGame = new QuizGame(4);
        quizGame.setGame(game);
        quizGameRepository.save(quizGame);
        messageService.startGame(quizzdto, gameInfoDTO.getGameId(),gameInfoDTO.getEventId(), gameInfoDTO.getStartedAt());
        List<Quiz> quizzes = quizzdto.stream().map(quizz-> new Quiz(quizz, game.getIdGame())).collect(Collectors.toList());
        quizService.saveQuizzes(quizzes);
        return ResponseEntity.ok("Save successfully");
    }


    @PostMapping("/{id_game}/player/{id_player}")
    public ResponseEntity<?> playShakeGame(@PathVariable Long id_game, @PathVariable Long id_player) throws Exception {
        try {
            PlaySession playSession = playSessionService.findOrCreatePlaySession(id_game, id_player);
            if (playSession.getTurns() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequest("Không đủ lượt để chơi!"));
            }

            List<RewardDTO> rewardsList = rewardService.getRewardListByIdPlayer(id_player).orElse(null);
            if (rewardsList == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi khi cố tải kho vật dụng của người dùng!"));
            }

            RewardDTO[] rewards = rewardsList.toArray(new RewardDTO[0]);

            double winProbability = 0.75;

            // Generate a random number between 0 and 1
            Random random = new Random();
            double randomValue = random.nextDouble();

            if (randomValue < winProbability) {
                int itemIndex = random.nextInt(rewards.length);
                RewardDTO wonItem = rewards[itemIndex];
                RewardDTO updatedReward;
                if (wonItem.getItemName().equals("Xu") || wonItem.getIdItem() == 5) {
                    Long coin = random.nextLong(200) + 1;
                    updatedReward = rewardService.incrementAmountByIdItemRepo(wonItem.getIdItemRepo(), coin);
                } else {
                    updatedReward = rewardService.incrementAmountByIdItemRepo(wonItem.getIdItemRepo(), null);
                }
                if (updatedReward == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Rất tiếc. Có lỗi xảy ra khi nhận thưởng!"));
                }
                playSessionRepository.decrementTurns(id_player, id_game);
                RewardDetail rewardDetail;
                if  (playSession.getTurns() > 0) {
                    rewardDetail = new RewardDetail(updatedReward, playSession.getTurns() - 1);
                } else {
                    rewardDetail = new RewardDetail(updatedReward, playSession.getTurns());
                }
                return ResponseEntity.ok(new SuccessResponse("Bạn đã trúng được 1 " + updatedReward.getItemName(), HttpStatus.OK, rewardDetail));
            } else {
                return ResponseEntity.ok(new SuccessResponse("Hụt mất rồi. Hãy thử lại vào lần sau nhé!", HttpStatus.OK, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Changing password failed by server!"));
        }
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

    @PutMapping("/game-info")
    public ResponseEntity<GameInfoDTO>  updateGameInfo(@RequestBody GameInfoDTO gameInfoDTO){
        Game game = gameRepository.findByIdGame(gameInfoDTO.getGameId());
        game.setName(gameInfoDTO.getName());
        gameRepository.save(game);
        if (gameInfoDTO.getGameType().equals("quiz-game")) {
            List<QuizDTO> quizDTOS = gameInfoDTO.getQuiz();
            for (QuizDTO quizDTO : quizDTOS) {
                Optional<Quiz> existingQuizOpt = quizRepository.findById(quizDTO.getQuizId());
                if (existingQuizOpt.isPresent()) {
                    Quiz existingQuiz = existingQuizOpt.get();
                    existingQuiz.setAns1(quizDTO.getAns1());
                    existingQuiz.setAns2(quizDTO.getAns2());
                    existingQuiz.setAns3(quizDTO.getAns3());
                    existingQuiz.setCorrectAnswerIndex(quizDTO.getCorrectAnswerIndex());
                    quizRepository.save(existingQuiz);
                } else {
                    Quiz newQuiz = new Quiz();
                    newQuiz.setQuestion(quizDTO.getQuestion());
                    newQuiz.setAns1(quizDTO.getAns1());
                    newQuiz.setAns2(quizDTO.getAns2());
                    newQuiz.setAns3(quizDTO.getAns3());
                    newQuiz.setCorrectAnswerIndex(quizDTO.getCorrectAnswerIndex());
                    newQuiz.setIdGame(gameInfoDTO.getGameId());
                    quizRepository.save(newQuiz);
                }
            }
        }
        return ResponseEntity.ok(gameInfoDTO);
    }

    @GetMapping("/{idGame}/players/{idPlayer}/turns")
    public ResponseEntity<?> getTurns(@PathVariable Long idGame, @PathVariable Long idPlayer) {
        try {
            PlaySession playSession = playSessionService.findPlaySessionByIdGameAndIdPlayer(idGame, idPlayer);
            if (playSession == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Không tìm thấy lượt chơi"));
            }
            return ResponseEntity.ok(new SuccessResponse("Truy cập số lượt còn lại của người chơi thành công!", HttpStatus.OK, playSession));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi khi cố truy cập số lượt chơi của người dùng!"));
        }
    }

//    public boolean checkTurnRecords(Long idPlayer, Long idGame) {
//        return playSessionRepository.decrementTurns(idPlayer, idGame) == 1;
//    }

    @PostMapping("/turns")
    public ResponseEntity<ApiResponse> giftTurns(@RequestBody GiftTurnRequest giftTurnRequest) {
        try {
            PlayerDTO playerDTO = null;
            if (giftTurnRequest.getEmail() != null) {
                playerDTO = playerService.findPlayerByIdentifier(giftTurnRequest.getEmail(), null, null).orElse(null);
            } else if (giftTurnRequest.getUsername() != null) {
                playerDTO = playerService.findPlayerByIdentifier(null, giftTurnRequest.getUsername(), null).orElse(null);
            } else if (giftTurnRequest.getReceiverId() != null) {
                playerDTO = playerService.findPlayerByIdentifier(null, null, giftTurnRequest.getReceiverId()).orElse(null);
            }
            if (playerDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Không tìm thấy người dùng hoặc có lỗi khi tìm kiếm người dùng"));
            }
            int turns = giftTurnRequest.getTurns();
            PlaySession playSession = playSessionService.findPlaySessionByIdGameAndIdPlayer(giftTurnRequest.getIdGame(), giftTurnRequest.getSenderId());
            if (playSession == null) {
                return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi hệ thống khi cố truy cập thông tin lượt chơi của người tặng!"));
            }
            if (playSession.getTurns() == 0 || playSession.getTurns() - turns < 0) {
                return ResponseEntity.ok(new SuccessResponse("Không đủ lượt để tặng", HttpStatus.OK, null));
            }
            playSessionRepository.increaseTurns(playerDTO.getIdUser(), giftTurnRequest.getIdGame(), turns);
            playSessionRepository.decreaseTurns(giftTurnRequest.getSenderId(), giftTurnRequest.getIdGame(), turns);
//            PlaySession receiver = playSessionService.findPlaySessionByIdGameAndIdPlayer(giftTurnRequest.getIdGame(), giftTurnRequest.getReceiverId());
//            PlaySession sender = playSessionService.findPlaySessionByIdGameAndIdPlayer(giftTurnRequest.getIdGame(), giftTurnRequest.getSenderId());
            return ResponseEntity.ok(new SuccessResponse("Tặng thành công", HttpStatus.OK, null));
        } catch (Exception e ) {
            return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi hệ thống khi tặng lượt chơi!"));
        }
    }

    @GetMapping("/{idGame}")
    public ResponseEntity<?> findGameByIdGame(@PathVariable Long idGame) {
        try {
            Game game = gameService.findGameByIdGame(idGame);
            if (game == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(game);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/turns/{idUser}/{idGame}")
    public ResponseEntity<ApiResponse> shareToGetTurn(@PathVariable Long idUser, @PathVariable Long idGame) {
        PlaySession playSession = playSessionService.findPlaySessionByIdGameAndIdPlayer(idGame, idUser);
        if (playSession == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Không tìm thấy người dùng hoặc trò chơi"));
        }
        try {
            playSessionService.shareToGetTurns(idGame, idUser);
            // Call api share count
            Game game = gameService.findGameByIdGame(idGame);
            eventClient.increaseShareCount(game.getIdEvent());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi hệ thống khi share lượt: " + e.getMessage()));
        }
        playSession.setTurns(playSession.getTurns() + 1);
        return ResponseEntity.ok(new SuccessResponse("Chúc mừng bạn đã nhận được 1 lượt", HttpStatus.OK, playSession));
    }

    @GetMapping("/events/{id_event}")
    public ResponseEntity<?> findGameByIdEvent(@PathVariable Long id_event) {
        try {
            Game game = gameService.findGameByIdEvent(id_event);
            return ResponseEntity.ok(game);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id_event}/participants")
    public ResponseEntity<?> findParticipantsByEvent(@PathVariable Long id_event) {
        try {
            int numberParticipants = playSessionService.countParticipantsByIdEvent(id_event);
            return ResponseEntity.ok(numberParticipants);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}


