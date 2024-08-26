package com.vou.event_service.service;


import com.vou.event_service.common.InternalServerError;
import com.vou.event_service.common.NotFoundException;
import com.vou.event_service.dto.GameInfoDTO;
import com.vou.event_service.dto.InventoryDTO;
import com.vou.event_service.dto.InventoryDetailDTO;
import com.vou.event_service.dto.InventoryImageUrlDTO;
import com.vou.event_service.entity.MultipartInputStreamFileResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    @Autowired
    private RestTemplate restTemplate;

    private final String QUIZ_SERVICE_URL = "http://inventory-and-reward-service:8087/api/v1/inventory-and-reward/vouchers";

    public void createInventory(InventoryDTO inventoryDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        // Wrap the List<QuizDTO> in an HttpEntity
        HttpEntity<InventoryDTO> request = new HttpEntity<>(inventoryDTO, headers);

        // Make the POST request
        ResponseEntity<Void> response = restTemplate.exchange(
                QUIZ_SERVICE_URL,
                HttpMethod.POST,
                request,
                Void.class
        );

        // Optionally handle the response
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Quizzes created successfully.");
        } else {
            System.out.println("Failed to create quizzes: " + response.getStatusCode());
        }
    }
    public InventoryDetailDTO getInventoryInfo(Long eventId) {
        String url = "http://inventory-and-reward-service:8087/api/v1/inventory-and-rewards/vouchers/voucher-info?eventId=" + eventId;

        // Make the GET request
        ResponseEntity<InventoryDetailDTO> response = restTemplate.getForEntity(url, InventoryDetailDTO.class);

        // Return the body of the response
        return response.getBody();
    }

    public InventoryImageUrlDTO uploadInventoryImages(String code, MultipartFile qrImg, MultipartFile voucherImg) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("qrImg", new MultipartInputStreamFileResource(qrImg.getInputStream(), qrImg.getOriginalFilename()));
            body.add("voucherImg", new MultipartInputStreamFileResource(voucherImg.getInputStream(), voucherImg.getOriginalFilename()));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String url = QUIZ_SERVICE_URL + "?code=" + code;

            ResponseEntity<InventoryImageUrlDTO> response = restTemplate.exchange(
                    url
                    , HttpMethod.PUT,
                    requestEntity,
                    InventoryImageUrlDTO.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
