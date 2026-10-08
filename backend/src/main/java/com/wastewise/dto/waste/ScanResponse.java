package com.wastewise.dto.waste;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @Builder
public class ScanResponse {
    private Long id;
    private String imageUrl;
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
    private String areaName;
    private LocalDateTime createdAt;
}
