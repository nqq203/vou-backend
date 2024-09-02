package com.vou.reward_service.service;

import com.vou.reward_service.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private RestTemplate restTemplate;

    private final String PLAYERS_SERVICE_URL = "http://localhost:8082/api/v1/players";

    public LinkedHashMap<String, Object> findPlayerById(Long idPlayer) throws Exception {
        String url = PLAYERS_SERVICE_URL + "/" + idPlayer;
        try {
            return (LinkedHashMap<String, Object>) restTemplate.getForEntity(url, Object.class).getBody();
        } catch (ResourceAccessException e) {
            throw new Exception("Không thể kết nối tới hệ thống quản trị người dùng");
        } catch (HttpClientErrorException.NotFound notFoundException) {
            throw new Exception("Không tìm thấy người nhận hoặc người gửi với id đã cung cấp");
        } catch (HttpServerErrorException.InternalServerError e) {
            throw new Exception("Lỗi hệ thống quản trị người dùng");
        }
    }
}
