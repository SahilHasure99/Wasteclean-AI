package com.wastewise.service;

import com.wastewise.dto.citizen.CitizenDashboardResponse;
import com.wastewise.dto.waste.ScanResponse;
import com.wastewise.entity.User;
import com.wastewise.entity.WasteScan;
import com.wastewise.repository.UserRepository;
import com.wastewise.repository.WasteScanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitizenService {

    private final UserRepository userRepository;
    private final WasteScanRepository scanRepository;
    private final WasteService wasteService;

    public CitizenDashboardResponse getDashboard(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        long totalScans = scanRepository.countByUserId(userId);
        long correctScans = scanRepository.countByUserIdAndCorrectlySegregated(userId, true);
        double correctRate = totalScans > 0 ? (double) correctScans / totalScans * 100.0 : 0.0;
        double segregationScore = totalScans > 0 ? Math.min(100.0, correctRate * 1.1) : 0.0;

        List<ScanResponse> recentScans = scanRepository
                .findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 5))
                .map(this::toScanResponse)
                .getContent();

        return CitizenDashboardResponse.builder()
                .segregationScore(Math.round(segregationScore * 10.0) / 10.0)
                .segregationGrade(gradeFromScore(segregationScore))
                .totalScans(totalScans)
                .correctSegregationRate(Math.round(correctRate * 10.0) / 10.0)
                .points(user.getPoints())
                .streak(user.getStreak())
                .badge(user.getBadge())
                .recentScans(recentScans)
                .build();
    }

    private String gradeFromScore(double score) {
        if (score >= 90) return "Excellent";
        if (score >= 75) return "Good";
        if (score >= 50) return "Needs Improvement";
        return "Critical";
    }

    private ScanResponse toScanResponse(WasteScan s) {
        return ScanResponse.builder()
                .id(s.getId()).imageUrl(s.getImageUrl()).category(s.getCategory())
                .subType(s.getSubType()).confidence(s.getConfidence())
                .contamination(s.getContamination()).conditionDesc(s.getConditionDesc())
                .recyclable(s.getRecyclable()).disposalCategory(s.getDisposalCategory())
                .recommendation(s.getRecommendation()).explanation(s.getExplanation())
                .impactScore(s.getImpactScore()).impactReason(s.getImpactReason())
                .correctlySegregated(s.getCorrectlySegregated())
                .areaName(s.getArea() != null ? s.getArea().getName() : null)
                .createdAt(s.getCreatedAt()).build();
    }
}
