package com.vou.statistics_service.controller;

import com.vou.statistics_service.common.ApiResponse;
import com.vou.statistics_service.common.SuccessResponse;
import com.vou.statistics_service.model.SavePlayerRankRequest;
import com.vou.statistics_service.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistics")
@CrossOrigin
public class StatisticsController {
    @Autowired
    private StatisticsService statisticService;

    @GetMapping("/events/{id_event}")
    public ResponseEntity<ApiResponse> getStatisticsByEventId(@PathVariable Long id_event) {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Thong ke su kien", HttpStatus.OK, statisticService.getStatistics(id_event)));
    }

    @PostMapping("/player-result")
    public ResponseEntity<?> saveQuizWinner(@RequestBody SavePlayerRankRequest request) {
        try {
            boolean result = statisticService.savePlayerResults(request);
            statisticService.updateQuizParticipants(request.getList().getFirst().getIdEvent(), (long) request.getList().size());
            if (result)
                return ResponseEntity.ok("Successfully save the players' result");
            return ResponseEntity.internalServerError().body("Error saving player result");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu kết quả trò chơi " + e.getMessage());
        }
    }

//    @PutMapping("/quiz-participants")
//    public ResponseEntity<?> saveQuizParticipants(@RequestBody SaveQuizParticipantRequest request) {
//        try {
//            return ResponseEntity.ok(statisticService.updateQuizParticipants(request));
//        } catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu thống kê người chơi Quiz: " + e.getMessage());
//        }
//    }

//    @PutMapping("/quiz-question-stats")
//    public ResponseEntity<?> saveQuizQuestionStat(@RequestBody CreateQuizQuestionStatRequest request) {
//        try {
//            return ResponseEntity.ok(statisticService.updateQuestionResultCount(request));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu thống kê câu hỏi Quiz: " + e.getMessage());
//        }
//    }
}
