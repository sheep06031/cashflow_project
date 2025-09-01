package com.example.cashflow.service;

import com.example.cashflow.dto.ApiRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api-key}")
    private String apiKey;

    public ApiRespDto<String> callChatGPT(Map<String, Object> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        String url = "https://api.openai.com/v1/chat/completions";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        try {
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("choices")) {
                var choices = (List<Map<String, Object>>) body.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");

                    return new ApiRespDto<>("success", "GPT response retrieved successfully", content);
                }
            }
            return new ApiRespDto<>("failed", "No response from GPT", null);
        } catch (Exception e) {
            return new ApiRespDto<>("failed", e.getMessage(), null);
        }
    }
}
