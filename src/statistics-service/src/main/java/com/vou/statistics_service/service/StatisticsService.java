package com.vou.statistics_service.service;

import com.vou.statistics_service.model.*;
import com.vou.statistics_service.repository.QuizGameStatsRepository;
import com.vou.statistics_service.repository.QuizQuestionStatsRepository;
import com.vou.statistics_service.repository.QuizWinnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    @Autowired
    private GameClient gameClient;
    @Autowired
    private EventClient eventClient;
    @Autowired
    private QuizWinnerRepository quizWinnerRepository;
    @Autowired
    private QuizGameStatsRepository quizGameStatsRepository;
    @Autowired
    private QuizQuestionStatsRepository quizQuestionStatsRepository;
    @Autowired
    private UserClient userClient;


    public StatisticsResponse getStatistics(Long id_event) {
        Optional<Game> game = gameClient.getGameByIdEvent(id_event);

        if (game.isPresent() && Objects.equals(game.get().getType(), "shake-game")) {
            System.out.println("Shake Game debugging");
            // Call API count play session bên GameClient
            Optional<Integer> participants = gameClient.getParticipantsCount(id_event);
            Integer participantCount = null;
            if (participants.isPresent()) {
                participantCount = participants.get();
                System.out.println("Participant count: " + participantCount);
            }
            // Call API Lấy total voucher, remaining voucher, share count của event
            Optional<Event> event = eventClient.getEvent(id_event);
            Event getEvent = null;
            if (event.isPresent()) {
                getEvent = event.get();
                System.out.println("Event count: " + getEvent);
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
                List<QuizWinner> winners = quizWinnerRepository.findQuizWinnersByIdEventAndIdGame(id_event, game.get().getIdGame());
                List<Long> requestUserIds = winners.stream().map(QuizWinner::getWinnerId).collect(Collectors.toList());
                Optional<List<User>> users = userClient.getUsers(requestUserIds);
                List<User> userList = null;
                if (users.isPresent()) {
                    userList = users.get();
                }
                // Lấy danh sách thống kê câu hỏi
                List<QuizQuestionStats> quizQuestionStats = quizQuestionStatsRepository.findQuizQuestionStatsByIdEventAndIdGame(id_event, game.get().getIdGame());
                List<QuestionCorrectRates> correctRates = quizQuestionStats.stream().map(quizQuestionStats1 -> new QuestionCorrectRates(quizQuestionStats1.getIdQuizQuestion(),  ((double) quizQuestionStats1.getCorrectCount() / (double) (quizQuestionStats1.getCorrectCount() + quizQuestionStats1.getIncorrectCount())))).collect(Collectors.toList());
                // Lấy số người tham gia
                QuizGameStats gameStats =  quizGameStatsRepository.findByIdEventAndIdGame(id_event, game.get().getIdGame());
                Long participant = gameStats.getNumberOfParticipants();
                QuizStatistics statistics = new QuizStatistics(participant);
                statistics.setCorrectRates(correctRates);
                statistics.setWinners(userList);
                return statistics;
            }
        }

        return null;
    }

    public QuizWinner saveWinner(CreateQuizWinnerRequest request) throws Exception {
        try {
            QuizWinner quizWinner = new QuizWinner(request.getIdEvent(), request.getIdGame(), request.getRank(), request.getUserId());
            return quizWinnerRepository.save(quizWinner);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public QuizGameStats updateQuizParticipants(CreateQuizGameStatRequest request) throws Exception {
        try {
            QuizGameStats quizGameStats = quizGameStatsRepository.findByIdEventAndIdGame(request.getIdEvent(), request.getIdGame());
            if (quizGameStats != null) {
                quizGameStats.setNumberOfParticipants(quizGameStats.getNumberOfParticipants() + 1);
                return quizGameStatsRepository.save(quizGameStats);
            } else {
                QuizGameStats newQuizGameStats = new QuizGameStats();
                newQuizGameStats.setNumberOfParticipants(1L);
                newQuizGameStats.setIdEvent(request.getIdEvent());
                newQuizGameStats.setIdGame(request.getIdGame());
                return quizGameStatsRepository.save(newQuizGameStats);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public QuizQuestionStats updateQuestionResultCount(CreateQuizQuestionStatRequest request) throws Exception {
        try {
            QuizQuestionStats stats = quizQuestionStatsRepository.findByIdQuizQuestion(request.getIdQuizQuestion());
            if (stats != null) {
                if (request.getResult()) {
                    stats.setCorrectCount(stats.getCorrectCount() + 1);
                } else {
                    stats.setIncorrectCount(stats.getIncorrectCount() + 1);
                }
                return quizQuestionStatsRepository.save(stats);
            } else {
                QuizQuestionStats newStats = new QuizQuestionStats();
                newStats.setIdQuizQuestion(request.getIdQuizQuestion());
                newStats.setCorrectCount(0L);
                newStats.setIncorrectCount(0L);
                newStats.setIdEvent(request.getIdEvent());
                newStats.setIdGame(request.getIdGame());
                if (request.getResult()) {
                    newStats.setCorrectCount(newStats.getCorrectCount() + 1);
                } else {
                    newStats.setIncorrectCount(newStats.getIncorrectCount() + 1);
                }
                return quizQuestionStatsRepository.save(newStats);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
