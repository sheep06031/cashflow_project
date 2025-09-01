package com.example.cashflow.controller.chatGPT;

import com.example.cashflow.dto.AiFeedBack.AiFeedBackReqDto;
import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.security.model.PrincipalUser;
import com.example.cashflow.service.GptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/gpt")
public class GptController {
    @Autowired
    private GptService gptService;

    @PostMapping("/feedback")
    public ResponseEntity<?> askGpt(@RequestBody AiFeedBackReqDto aiFeedBackReqDto, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(gptService.callChatGPT(aiFeedBackReqDto, principalUser));
    }

    @GetMapping("/feedback/result")
    public ResponseEntity<?> getFeedback(@AuthenticationPrincipal PrincipalUser principalUser, String date) {
        return ResponseEntity.ok(gptService.getFeedbackByUserIdAndDate(principalUser, date));
    }
}
