package com.example.cashflow.service;

import com.example.cashflow.Entity.AiFeedBack;
import com.example.cashflow.Entity.Transaction;
import com.example.cashflow.Entity.User;
import com.example.cashflow.dto.AiFeedBack.AiFeedBackReqDto;
import com.example.cashflow.dto.ApiRespDto;
import com.example.cashflow.repository.AiFeedbackRepository;
import com.example.cashflow.repository.UserRepository;
import com.example.cashflow.security.model.PrincipalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GptService {

    @Autowired
    private AiFeedbackRepository aiFeedbackRepository;

    @Autowired
    private UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api-key}")
    private String apiKey;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> callChatGPT(AiFeedBackReqDto aiFeedBackReqDto, PrincipalUser principalUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        LocalDate parsedDate = LocalDate.parse(aiFeedBackReqDto.getDate() + "-01");


        Optional<AiFeedBack> optionalAiFeedBack = aiFeedbackRepository.getAiFeedbackByUserIdAndDate(principalUser.getUserId(), parsedDate);
        if(optionalAiFeedBack.isPresent() && optionalAiFeedBack.get().getCount() > 2) {
           return new ApiRespDto<>("expired", "You have used all chances for this month", null);
        }

        Map<String, Object> request = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content",
                                "You are a financial advisor AI specializing in personal budgeting and spending analysis. " +
                                        "You will receive transaction data for a specific month along with the previous month’s data. " +
                                        "Your task is to:\n\n" +
                                        "1. Analyze the user’s spending patterns and highlight any noticeable trends or changes.\n" +
                                        "2. Provide clear and practical insights.\n" +
                                        "3. Suggest actionable recommendations for improving their spending habits.\n" +
                                        "4. Keep your answer well-structured with clear headings and bullet points.\n" +
                                        "5. Provide detailed explanations and reasoning with reference to the data.\n" +
                                        "6. Always explain reasoning with reference to the data.\n\n" +
                                        "⚠️ Respond ONLY with a raw JSON object. Do not include explanations outside of JSON, " +
                                        "do not wrap with markdown code fences (```), and do not return any other keys.\n" +
                                        "✅ The JSON must strictly have this format:\n" +
                                        "{ \"english\": \"(Markdown-formatted feedback in English)\", " +
                                        "\"korean\": \"(Markdown-formatted feedback in Korean)\" }"),

                        Map.of("role", "user", "content",
                                String.format("{\"date\": \"%s\", \"currentMonthData\": %s, \"previousMonthData\": %s}",
                                        aiFeedBackReqDto.getDate(),
                                        aiFeedBackReqDto.getCurrentMonthData(),
                                        aiFeedBackReqDto.getPreviousMonthData()
                                )
                        )
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        String url = "https://api.openai.com/v1/chat/completions";

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> body = response.getBody();

            if (body != null && body.containsKey("choices")) {
                var choices = (List<Map<String, Object>>) body.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");

                    content = content.trim();
                    if (content.startsWith("```")) {
                        content = content.replaceAll("```(json)?", "").trim();
                    }

                    com.fasterxml.jackson.databind.ObjectMapper objectMapper =
                            new com.fasterxml.jackson.databind.ObjectMapper()
                                    .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
                                    .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);


                    Map<String, String> parsed = objectMapper.readValue(content, Map.class);



                    String englishFeedback = parsed.get("english");
                    String koreanFeedback = parsed.get("korean");




                    if (optionalAiFeedBack.isPresent()) {
                        AiFeedBack existing = optionalAiFeedBack.get();
                        int newCount = existing.getCount() + 1;

                        existing.setFeedbackEng(englishFeedback);
                        existing.setFeedbackKr(koreanFeedback);
                        existing.setCount(newCount);

                        aiFeedbackRepository.updateAiFeedback(existing);
                        Map<String, Object> resultData = Map.of(
                                "english", englishFeedback,
                                "korean", koreanFeedback,
                                "count", 3 - newCount
                        );
                        return new ApiRespDto<>("success", "GPT feedback updated successfully", resultData);
                    } AiFeedBack aiFeedBack = AiFeedBack.builder()
                            .userId(principalUser.getUserId())
                            .date(parsedDate)
                            .feedbackEng(englishFeedback)
                            .feedbackKr(koreanFeedback)
                            .count(1)
                            .build();
                    Map<String, Object> resultData = Map.of(
                            "english", englishFeedback,
                            "korean", koreanFeedback,
                            "count", 2
                    );
                    int result = aiFeedbackRepository.addAiFeedback(aiFeedBack);
                    if (result != 1) return new ApiRespDto<>("failed", "Failed to save GPT feedback to DB", null);
                    return new ApiRespDto<>("success", "GPT response retrieved successfully", resultData);
                }
            }
            return new ApiRespDto<>("failed", "No response from GPT", null);
        } catch (Exception e) {
            return new ApiRespDto<>("failed", e.getMessage(), null);
        }
    }

    public ApiRespDto<?> getFeedbackByUserIdAndDate(PrincipalUser principalUser, String date) {
        if (principalUser == null || principalUser.getUserId() == null) {
            return new ApiRespDto<>("failed", "Invalid access. Please log in again", null);
        }

        LocalDate parsedDate = LocalDate.parse(date + "-01");

        Optional<AiFeedBack> optionalAiFeedBack = aiFeedbackRepository.getFeedbackByUserIdAndDate(principalUser.getUserId(), parsedDate);

        try {
            if (optionalAiFeedBack.isEmpty()) {
                return new ApiRespDto<>("failed", "No Ai Feedback found", null);
            } else {
                AiFeedBack feedBack = optionalAiFeedBack.get();
                return new ApiRespDto<>("success", "Ai FeedBack retrieved successfully", Map.of(
                        "english", feedBack.getFeedbackEng(),
                        "korean", feedBack.getFeedbackKr(),
                        "count", 3 - feedBack.getCount()
                ));
            }
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "An error has occurred" + e.getMessage(), null);
        }
    }

    public ApiRespDto<?> getFeedbackListByUserId(PrincipalUser principalUser) {
        if (principalUser == null || principalUser.getUserId() == null) {
            return new ApiRespDto<>("failed", "Invalid access. Please log in again", null);
        }

        Optional<User> user = userRepository.getUserByUserId(principalUser.getUserId());
        if (user.isEmpty()) {
            return new ApiRespDto<>("failed", "User not found", null);
        }

        List<AiFeedBack> feedbackList = aiFeedbackRepository.getFeedbackListByUserId(principalUser.getUserId());

        try {
            if (feedbackList.isEmpty()) {
                return new ApiRespDto<>("failed", "No Feedback found", null);
            } else {
                return new ApiRespDto<>("success", "Feedbacks retrieved successfully", feedbackList);
            }
        } catch (Exception e) {
            return new ApiRespDto<>("failed", "An error has occurred" + e.getMessage(), null);
        }
    }
}