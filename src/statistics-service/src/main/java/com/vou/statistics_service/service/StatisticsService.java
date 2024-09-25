package com.vou.statistics_service.service;

import com.vou.statistics_service.model.*;
import com.vou.statistics_service.repository.QuizGameStatsRepository;
import com.vou.statistics_service.repository.QuizQuestionStatsRepository;
import com.vou.statistics_service.repository.PlayerResultRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    @Autowired
    private GameClient gameClient;
    @Autowired
    private EventClient eventClient;
    @Autowired
    private PlayerResultRepository playerResultRepository;
    @Autowired
    private QuizGameStatsRepository quizGameStatsRepository;
    @Autowired
    private QuizQuestionStatsRepository quizQuestionStatsRepository;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ModelMapper mapper;


    public StatisticsResponse getStatistics(Long id_event) {
        Optional<Game> game = gameClient.getGameByIdEvent(id_event);

        if (game.isPresent() && Objects.equals(game.get().getType(), "shake-game")) {
            // Call API count play session bên GameClient
            Optional<Integer> participants = gameClient.getParticipantsCount(id_event);
            Integer participantCount = null;
            if (participants.isPresent()) {
                participantCount = participants.get();
            }
            // Call API Lấy total voucher, remaining voucher, share count của event
            Optional<Event> event = eventClient.getEvent(id_event);
            Event getEvent = null;
            if (event.isPresent()) {
                getEvent = event.get();
            }
            if (participantCount != null && getEvent != null) {
                ShakeStatistics statistics = new ShakeStatistics((long) participantCount);
                statistics.setGivenVouchers((long) (getEvent.getNumberOfVouchers() - getEvent.getRemainingVouchers()));
                statistics.setRemainingVouchers((long) getEvent.getRemainingVouchers());
                statistics.setShareCount(getEvent.getShareCount());
                return statistics;
            }
        } else {
            if (game.isPresent()) {
                // Lấy thông tin người chiến thắng từ DB
                List<PlayerResult> winners = playerResultRepository.findPlayerResultByIdEvent(id_event);
                if (winners == null)
                {
                    System.out.println("Winner is null");
                    return new QuizStatistics();
                }

                List<String> requestUsernames = winners.stream().map(PlayerResult::getPlayerUsername).collect(Collectors.toList());
                Optional<List<User>> users = userClient.getUsers(requestUsernames);
                List<User> userList = null;
                if (users.isEmpty()) {
                    System.out.println("User list is empty");
                    return new QuizStatistics();
                }
                userList = users.get();
                List<QuizWinnerMetadata> metadata = userList.stream().map(user -> {
                    QuizWinnerMetadata quizWinnerMetadata = mapper.map(user, QuizWinnerMetadata.class);
                    // Tìm PlayerResult tương ứng với user
                    Optional<PlayerResult> correspondingWinner = winners.stream()
                            .filter(winner -> winner.getPlayerUsername().equals(quizWinnerMetadata.getUsername()))
                            .findFirst();

                    // Thiết lập giá trị rank nếu tìm thấy người thắng tương ứng
                    correspondingWinner.ifPresent(winner -> quizWinnerMetadata.setRank(winner.getRank()));

                    return quizWinnerMetadata;
                })
                        .sorted(Comparator.comparingInt(QuizWinnerMetadata::getRank)).toList();
                // Lấy danh sách thống kê câu hỏi
                //List<QuizQuestionStats> quizQuestionStats = quizQuestionStatsRepository.findQuizQuestionStatsByIdEventAndIdGame(id_event, game.get().getIdGame());
                List<QuestionCorrectRates> correctRates = new ArrayList<>();//quizQuestionStats.stream().map(quizQuestionStats1 -> new QuestionCorrectRates(quizQuestionStats1.getIdQuizQuestion(),  ((double) quizQuestionStats1.getCorrectCount() / (double) (quizQuestionStats1.getCorrectCount() + quizQuestionStats1.getIncorrectCount())))).collect(Collectors.toList());
                // Lấy số người tham gia
                QuizGameStats gameStats =  quizGameStatsRepository.findByIdEvent(id_event);
                if (gameStats == null) {
                    return new QuizStatistics();
                }
                Long participant = gameStats.getNumberOfParticipants();
                QuizStatistics statistics = new QuizStatistics(participant);
                statistics.setCorrectRates(correctRates);
                statistics.setWinners(metadata);
                return statistics;
            }
        }

        return null;
    }

    public boolean savePlayerResults(SavePlayerRankRequest request) throws Exception {
        try {
            List<PlayerFinalRank> playerFinalRankList = request.getList();
            for (PlayerFinalRank playerFinalRank : playerFinalRankList) {
                playerResultRepository.save(mapper.map(playerFinalRank, PlayerResult.class));
            }
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public QuizGameStats updateQuizParticipants(Long idEvent, Long numberParticipants) throws Exception {
        try {
            QuizGameStats quizGameStats = new QuizGameStats(idEvent, numberParticipants) ;
            return quizGameStatsRepository.save(quizGameStats);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

//    public QuizQuestionStats updateQuestionResultCount(CreateQuizQuestionStatRequest request) throws Exception {
//        try {
//            QuizQuestionStats stats = quizQuestionStatsRepository.findByIdQuizQuestion(request.getIdQuizQuestion());
//            if (stats != null) {
//                if (request.getResult()) {
//                    stats.setCorrectCount(stats.getCorrectCount() + 1);
//                } else {
//                    stats.setIncorrectCount(stats.getIncorrectCount() + 1);
//                }
//                return quizQuestionStatsRepository.save(stats);
//            } else {
//                QuizQuestionStats newStats = new QuizQuestionStats();
//                newStats.setIdQuizQuestion(request.getIdQuizQuestion());
//                newStats.setCorrectCount(0L);
//                newStats.setIncorrectCount(0L);
//                newStats.setIdEvent(request.getIdEvent());
//                newStats.setIdGame(request.getIdGame());
//                if (request.getResult()) {
//                    newStats.setCorrectCount(newStats.getCorrectCount() + 1);
//                } else {
//                    newStats.setIncorrectCount(newStats.getIncorrectCount() + 1);
//                }
//                return quizQuestionStatsRepository.save(newStats);
//            }
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }
}
