package com.wastewise.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data @AllArgsConstructor @Builder
public class AdminDashboardResponse {
    private Long totalScans;
    private Long totalReports;
    private Double correctSegregationRate;
    private Double mixedWasteRate;
    private Long criticalAreas;
    private Long pendingTasks;
    private Map<String, Long> categoryDistribution;
    private List<HotspotResponse> topHotspots;
    private List<PriorityResponse> topPriorities;
}
