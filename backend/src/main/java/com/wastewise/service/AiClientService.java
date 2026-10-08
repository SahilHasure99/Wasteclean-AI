package com.wastewise.service;

import com.wastewise.dto.ai.AiAnalysisResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.Map;

@Service
@Slf4j
public class AiClientService {

    private final RestClient restClient;

    public AiClientService(@Value("${app.ai-service.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public AiAnalysisResponse analyzeWaste(byte[] imageBytes) {
        try {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            Map<String, String> body = Map.of("image", base64Image);

            return restClient.post()
                    .uri("/analyze-waste")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(AiAnalysisResponse.class);
        } catch (Exception e) {
            log.error("AI service call failed: {}", e.getMessage());
            return fallbackAnalysis();
        }
    }

    public Map<String, Object> verifyCleanup(byte[] beforeImage, byte[] afterImage) {
        try {
            Map<String, String> body = Map.of(
                    "before_image", Base64.getEncoder().encodeToString(beforeImage),
                    "after_image", Base64.getEncoder().encodeToString(afterImage)
            );
            @SuppressWarnings("unchecked")
            Map<String, Object> result = restClient.post()
                    .uri("/verify-cleanup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);
            return result;
        } catch (Exception e) {
            log.error("Cleanup verification failed: {}", e.getMessage());
            return Map.of("status", "NEEDS_REVIEW", "confidence", 0.0,
                    "explanation", "AI service unavailable. Manual verification required.");
        }
    }

    /** Fallback when AI service is down — returns a safe default */
    private AiAnalysisResponse fallbackAnalysis() {
        return AiAnalysisResponse.builder()
                .category("Unknown")
                .subType("Unidentified")
                .confidence(0.0)
                .contamination("unknown")
                .conditionDesc("AI service unavailable")
                .recyclable(false)
                .disposalCategory("General Waste")
                .recommendation("Please try again later or consult local waste guidelines.")
                .explanation("The AI analysis service is currently unavailable.")
                .impactScore(50)
                .impactReason("Unable to assess — AI service offline.")
                .correctlySegregated(false)
                .build();
    }
}
