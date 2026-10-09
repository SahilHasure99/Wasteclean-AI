package com.wastewise.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @AllArgsConstructor @Builder
public class HotspotResponse {
    private Long areaId;
    private String areaName;
    private String ward;
    private Double riskScore;
    private String riskStatus;
    private String riskExplanation;
    private Integer totalScans;
    private Integer mixedWasteCount;
    private Integer openReports;
    private Double segregationScore;
}
