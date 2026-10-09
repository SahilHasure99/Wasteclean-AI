package com.wastewise.service.intelligence;

import com.wastewise.entity.AreaMetrics;
import com.wastewise.entity.PriorityTask;
import com.wastewise.enums.TaskStatus;
import com.wastewise.repository.AreaMetricsRepository;
import com.wastewise.repository.PriorityTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriorityEngine {

    private final AreaMetricsRepository metricsRepository;
    private final PriorityTaskRepository taskRepository;

    /**
     * Generates or updates priority tasks for all areas based on
     * risk score, waste volume, report frequency, and time factors.
     */
    public List<PriorityTask> generatePriorities() {
        List<AreaMetrics> allMetrics = metricsRepository.findAllByOrderByRiskScoreDesc();
        List<PriorityTask> tasks = new ArrayList<>();

        for (AreaMetrics metrics : allMetrics) {
            if (metrics.getRiskScore() < 30) continue; // skip low-risk areas

            int priority = calculatePriority(metrics);
            String action = recommendAction(priority);
            String justification = buildJustification(metrics);

            // Check existing active task for this area
            List<PriorityTask> existing = taskRepository
                    .findByAreaIdAndStatusNot(metrics.getArea().getId(), TaskStatus.COMPLETED);

            PriorityTask task;
            if (!existing.isEmpty()) {
                task = existing.get(0);
                task.setPriorityScore(priority);
                task.setRecommendedAction(action);
                task.setJustification(justification);
            } else {
                task = PriorityTask.builder()
                        .area(metrics.getArea())
                        .priorityScore(priority)
                        .recommendedAction(action)
                        .justification(justification)
                        .status(TaskStatus.PENDING)
                        .build();
            }
            tasks.add(taskRepository.save(task));
        }
        return tasks;
    }

    private int calculatePriority(AreaMetrics m) {
        double score = 0;
        // Risk score contributes 50%
        score += m.getRiskScore() * 0.5;
        // Volume contributes 20%
        score += Math.min(20, m.getTotalScans() * 0.4);
        // Open reports contribute 20%
        score += Math.min(20, m.getOpenReports() * 5.0);
        // Hazardous urgency 10%
        score += Math.min(10, m.getHazardousCount() * 3.0);

        return (int) Math.min(100, Math.max(0, score));
    }

    private String recommendAction(int priority) {
        if (priority >= 85) return "Immediate intervention required";
        if (priority >= 65) return "Schedule intervention within 24 hours";
        if (priority >= 45) return "Schedule intervention this week";
        return "Monitor and reassess";
    }

    private String buildJustification(AreaMetrics m) {
        List<String> points = new ArrayList<>();
        points.add("Risk score: " + m.getRiskScore() + "/100 (" + m.getRiskStatus() + ")");
        if (m.getTotalScans() > 0) {
            points.add("Segregation: " + String.format("%.0f%%", m.getSegregationScore()));
        }
        if (m.getMixedWasteCount() > 0) {
            points.add(m.getMixedWasteCount() + " mixed waste reports");
        }
        if (m.getOpenReports() > 0) {
            points.add(m.getOpenReports() + " unresolved reports");
        }
        if (m.getLastCollection() != null) {
            long days = ChronoUnit.DAYS.between(m.getLastCollection(), LocalDateTime.now());
            points.add("Last collection: " + days + " days ago");
        }
        return String.join(". ", points) + ".";
    }
}
