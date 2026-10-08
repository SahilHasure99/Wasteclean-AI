package com.wastewise.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AiAnalysisResponse {
    private String category;
    private String subType;
    private Double confidence;
    private String contamination;
    private String conditionDesc;
    private Boolean recyclable;
    private String disposalCategory;
    private String recommendation;
    private String explanation;
    private Integer impactScore;
    private String impactReason;
    private Boolean correctlySegregated;
}
