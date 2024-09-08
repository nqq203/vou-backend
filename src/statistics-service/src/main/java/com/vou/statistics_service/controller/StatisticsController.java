package com.vou.statistics_service.controller;

import com.vou.statistics_service.common.ApiResponse;
import com.vou.statistics_service.common.SuccessResponse;
import com.vou.statistics_service.model.CreateQuizGameStatRequest;
import com.vou.statistics_service.model.CreateQuizQuestionStatRequest;
import com.vou.statistics_service.model.CreateQuizWinnerRequest;
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

    @PostMapping("/quiz-winner")
    public ResponseEntity<?> saveQuizWinner(@RequestBody CreateQuizWinnerRequest request) {
        try {
            return ResponseEntity.ok(statisticService.saveWinner(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tạo người chiến thắng " + e.getMessage());
        }
    }

    @PutMapping("/quiz-participants")
    public ResponseEntity<?> saveQuizParticipants(@RequestBody CreateQuizGameStatRequest request) {
        try {
            return ResponseEntity.ok(statisticService.updateQuizParticipants(request));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu thống kê người chơi Quiz: " + e.getMessage());
        }
    }

    @PutMapping("/quiz-question-stats")
    public ResponseEntity<?> saveQuizQuestionStat(@RequestBody CreateQuizQuestionStatRequest request) {
        try {
            return ResponseEntity.ok(statisticService.updateQuestionResultCount(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu thống kê câu hỏi Quiz: " + e.getMessage());
        }
    }
}
