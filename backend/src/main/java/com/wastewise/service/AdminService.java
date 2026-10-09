package com.wastewise.service;

import com.wastewise.dto.admin.*;
import com.wastewise.entity.AreaMetrics;
import com.wastewise.entity.PriorityTask;
import com.wastewise.enums.RiskStatus;
import com.wastewise.enums.TaskStatus;
import com.wastewise.repository.*;
import com.wastewise.service.intelligence.AreaRiskEngine;
import com.wastewise.service.intelligence.PriorityEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final WasteScanRepository scanRepository;
    private final WasteReportRepository reportRepository;
    private final AreaMetricsRepository metricsRepository;
    private final PriorityTaskRepository taskRepository;
    private final AreaRiskEngine riskEngine;
    private final PriorityEngine priorityEngine;

    public AdminDashboardResponse getDashboard() {
        // Recalculate risks and priorities
        riskEngine.recalculateAll();

        long totalScans = scanRepository.count();
        long totalReports = reportRepository.count();

        // Category distribution
        Map<String, Long> catDist = new LinkedHashMap<>();
        scanRepository.countByCategories().forEach(row ->
                catDist.put((String) row[0], (Long) row[1]));

        // Correct segregation rate
        long correctScans = scanRepository.findAll().stream()
                .filter(s -> Boolean.TRUE.equals(s.getCorrectlySegregated())).count();
        double correctRate = totalScans > 0 ? (double) correctScans / totalScans * 100 : 0;

        // Mixed waste rate
        long mixedCount = catDist.getOrDefault("Mixed Waste", 0L);
        double mixedRate = totalScans > 0 ? (double) mixedCount / totalScans * 100 : 0;

        // Hotspots
        List<AreaMetrics> allMetrics = metricsRepository.findAllByOrderByRiskScoreDesc();
        long criticalAreas = allMetrics.stream()
                .filter(m -> m.getRiskStatus() == RiskStatus.CRITICAL || m.getRiskStatus() == RiskStatus.HIGH)
                .count();

        List<HotspotResponse> topHotspots = allMetrics.stream().limit(5)
                .map(this::toHotspot).collect(Collectors.toList());

        // Priorities
        long pendingTasks = taskRepository.countByStatus(TaskStatus.PENDING);
        List<PriorityResponse> topPriorities = taskRepository.findAllByOrderByPriorityScoreDesc()
                .stream().limit(5).map(this::toPriority).collect(Collectors.toList());

        return AdminDashboardResponse.builder()
                .totalScans(totalScans)
                .totalReports(totalReports)
                .correctSegregationRate(Math.round(correctRate * 10.0) / 10.0)
                .mixedWasteRate(Math.round(mixedRate * 10.0) / 10.0)
                .criticalAreas(criticalAreas)
                .pendingTasks(pendingTasks)
                .categoryDistribution(catDist)
                .topHotspots(topHotspots)
                .topPriorities(topPriorities)
                .build();
    }

    public List<HotspotResponse> getHotspots() {
        riskEngine.recalculateAll();
        return metricsRepository.findAllByOrderByRiskScoreDesc()
                .stream().map(this::toHotspot).collect(Collectors.toList());
    }

    public List<PriorityResponse> getPriorities() {
        riskEngine.recalculateAll();
        priorityEngine.generatePriorities();
        return taskRepository.findAllByOrderByPriorityScoreDesc()
                .stream().map(this::toPriority).collect(Collectors.toList());
    }

    private HotspotResponse toHotspot(AreaMetrics m) {
        return HotspotResponse.builder()
                .areaId(m.getArea().getId())
                .areaName(m.getArea().getName())
                .ward(m.getArea().getWard())
                .riskScore(m.getRiskScore())
                .riskStatus(m.getRiskStatus().name())
                .riskExplanation(m.getRiskExplanation())
                .totalScans(m.getTotalScans())
                .mixedWasteCount(m.getMixedWasteCount())
                .openReports(m.getOpenReports())
                .segregationScore(m.getSegregationScore())
                .build();
    }

    private PriorityResponse toPriority(PriorityTask t) {
        return PriorityResponse.builder()
                .taskId(t.getId())
                .areaId(t.getArea().getId())
                .areaName(t.getArea().getName())
                .priorityScore(t.getPriorityScore())
                .recommendedAction(t.getRecommendedAction())
                .justification(t.getJustification())
                .status(t.getStatus().name())
                .assignedToName(t.getAssignedTo() != null ? t.getAssignedTo().getName() : null)
                .build();
    }
}
