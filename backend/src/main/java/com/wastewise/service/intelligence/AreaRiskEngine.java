package com.wastewise.service.intelligence;

import com.wastewise.entity.AreaMetrics;
import com.wastewise.enums.RiskStatus;
import com.wastewise.repository.AreaMetricsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaRiskEngine {

    private final AreaMetricsRepository metricsRepository;

    /**
     * Recalculates the risk score for an area based on multiple weighted factors.
     * Score range: 0–100. Higher = more urgent.
     */
    public void recalculateRisk(AreaMetrics metrics) {
        List<String> reasons = new ArrayList<>();
        double score = 0.0;

        // Factor 1: Mixed waste percentage (weight: 30)
        if (metrics.getTotalScans() > 0) {
            double mixedPct = (double) metrics.getMixedWasteCount() / metrics.getTotalScans() * 100;
            double mixedContrib = Math.min(30, mixedPct * 0.6);
            score += mixedContrib;
            if (mixedPct > 30) reasons.add(String.format("Mixed waste at %.0f%% of total scans", mixedPct));
        }

        // Factor 2: Low segregation score (weight: 25)
        if (metrics.getTotalScans() > 0) {
            double segDeficit = 100.0 - metrics.getSegregationScore();
            double segContrib = segDeficit * 0.25;
            score += segContrib;
            if (metrics.getSegregationScore() < 60) {
                reasons.add(String.format("Segregation score is low at %.0f%%", metrics.getSegregationScore()));
            }
        }

        // Factor 3: Open reports (weight: 20)
        double reportContrib = Math.min(20, metrics.getOpenReports() * 4.0);
        score += reportContrib;
        if (metrics.getOpenReports() > 3) {
            reasons.add(metrics.getOpenReports() + " unresolved waste reports");
        }

        // Factor 4: Hazardous waste (weight: 15)
        double hazContrib = Math.min(15, metrics.getHazardousCount() * 5.0);
        score += hazContrib;
        if (metrics.getHazardousCount() > 0) {
            reasons.add(metrics.getHazardousCount() + " hazardous/e-waste items detected");
        }

        // Factor 5: Time since last collection (weight: 10)
        if (metrics.getLastCollection() != null) {
            long daysSince = ChronoUnit.DAYS.between(metrics.getLastCollection(), LocalDateTime.now());
            double timeContrib = Math.min(10, daysSince * 1.5);
            score += timeContrib;
            if (daysSince > 5) reasons.add("No collection activity for " + daysSince + " days");
        } else {
            score += 5;
            reasons.add("No collection activity recorded");
        }

        score = Math.min(100, Math.max(0, score));
        metrics.setRiskScore(Math.round(score * 10.0) / 10.0);
        metrics.setRiskStatus(statusFromScore(score));

        if (reasons.isEmpty()) {
            metrics.setRiskExplanation("Area metrics within normal parameters.");
        } else {
            metrics.setRiskExplanation(String.join(". ", reasons) + ".");
        }

        metricsRepository.save(metrics);
    }

    public void recalculateAll() {
        metricsRepository.findAll().forEach(this::recalculateRisk);
    }

    private RiskStatus statusFromScore(double score) {
        if (score >= 80) return RiskStatus.CRITICAL;
        if (score >= 60) return RiskStatus.HIGH;
        if (score >= 35) return RiskStatus.MEDIUM;
        return RiskStatus.LOW;
    }
}
