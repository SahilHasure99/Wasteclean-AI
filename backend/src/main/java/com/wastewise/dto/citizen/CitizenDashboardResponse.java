package com.wastewise.dto.citizen;

import com.wastewise.dto.waste.ScanResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @AllArgsConstructor @Builder
public class CitizenDashboardResponse {
    private Double segregationScore;
    private String segregationGrade;
    private Long totalScans;
    private Double correctSegregationRate;
    private Integer points;
    private Integer streak;
    private String badge;
    private List<ScanResponse> recentScans;
}
