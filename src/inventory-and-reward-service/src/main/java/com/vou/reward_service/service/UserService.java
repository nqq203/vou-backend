package com.vou.reward_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;


@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private RestTemplate restTemplate;

    private final String PLAYERS_SERVICE_URL = "http://user-service:8082/api/v1/players";

    public LinkedHashMap<String, Object> findPlayerByIdentifier(Object playerIdentifier, String type) throws Exception {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLAYERS_SERVICE_URL);

            switch (type) {
                case "id_user":
                    builder.queryParam("id_user", playerIdentifier);
                    break;
                case "email":
                    builder.queryParam("email", playerIdentifier);
                    break;
                case "username":
                    builder.queryParam("username", playerIdentifier);
                    break;
            }

            String url = builder.toUriString();
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
