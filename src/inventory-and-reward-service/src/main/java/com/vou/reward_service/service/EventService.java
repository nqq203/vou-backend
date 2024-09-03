package com.vou.reward_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
public class EventService {
    @Autowired
    private RestTemplate restTemplate;

    private final String EVENTS_SERVICE_URL = "http://event-service:8083/api/v1/events";

    public HttpStatusCode decreaseRemainingVoucher(Long id_event) throws Exception {
        String url = EVENTS_SERVICE_URL + "/" + id_event + "/remaining-vouchers";

        try {
            ResponseEntity<String> responseUpdateEvent = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    null,
                    String.class
            );

            return responseUpdateEvent.getStatusCode();
        } catch (
                ResourceAccessException e) {
            throw new Exception("Không thể kết nối tới hệ thống quản trị event");
        } catch (
                HttpClientErrorException.NotFound notFoundException) {
            throw new Exception("Không tìm thấy event với id đã cung cấp");
        } catch (
                HttpServerErrorException.InternalServerError e) {
            throw new Exception("Lỗi hệ thống quản trị event");
        } catch (Exception e) {
            throw new Exception("Lỗi hệ thống bất ngờ" + e.getMessage());
        }
    }
}
