package com.example.cashflow.controller.chatGPT;

import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.service.GptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/gpt")
public class GptController {
    @Autowired
    private GptService gptService;

    @PostMapping("/feedback")
    public ResponseEntity<?> askGpt(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(gptService.callChatGPT(request));
    }
}
