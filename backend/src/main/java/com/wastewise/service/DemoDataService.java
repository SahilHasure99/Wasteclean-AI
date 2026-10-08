package com.wastewise.service;

import com.wastewise.entity.Area;
import com.wastewise.entity.AreaMetrics;
import com.wastewise.entity.User;
import com.wastewise.enums.RiskStatus;
import com.wastewise.enums.Role;
import com.wastewise.repository.AreaMetricsRepository;
import com.wastewise.repository.AreaRepository;
import com.wastewise.repository.PriorityTaskRepository;
import com.wastewise.repository.UserRepository;
import com.wastewise.repository.WasteReportRepository;
import com.wastewise.repository.WasteScanRepository;
import com.wastewise.service.intelligence.AreaRiskEngine;
import com.wastewise.service.intelligence.PriorityEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DemoDataService {

    private final UserRepository userRepository;
    private final AreaRepository areaRepository;
    private final AreaMetricsRepository metricsRepository;
    private final WasteScanRepository scanRepository;
    private final WasteReportRepository reportRepository;
    private final PriorityTaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;
    private final AreaRiskEngine riskEngine;
    private final PriorityEngine priorityEngine;

    @Transactional
    public String seedDemoData() {
        if (userRepository.existsByEmail("admin@wastewise.ai")) {
            return "Demo data already seeded.";
        }

        // 1. Users
        String pwd = passwordEncoder.encode("password");
        User admin = userRepository.save(User.builder().name("Admin").email("admin@wastewise.ai").passwordHash(pwd).role(Role.ADMIN).build());
        User citizen = userRepository.save(User.builder().name("Citizen Rahul").email("citizen@wastewise.ai").passwordHash(pwd).role(Role.CITIZEN).points(120).streak(3).badge("🌱 Waste Starter").build());
        User collector = userRepository.save(User.builder().name("Collector Team A").email("collector@wastewise.ai").passwordHash(pwd).role(Role.COLLECTOR).build());

        // 2. Areas & Metrics
        createAreaWithData("Shivajinagar", "Ward 1", 15, 12, 1, 0, 1, RiskStatus.LOW, "Monitoring normal activity.", LocalDateTime.now().minusDays(1));
        createAreaWithData("Kothrud", "Ward 2", 42, 28, 14, 2, 4, RiskStatus.CRITICAL, "High mixed waste and open reports.", LocalDateTime.now().minusDays(3));
        createAreaWithData("Hinjawadi", "Ward 3", 8, 8, 0, 0, 0, RiskStatus.LOW, "Excellent segregation.", LocalDateTime.now().minusHours(5));
        createAreaWithData("Viman Nagar", "Ward 4", 25, 15, 8, 0, 2, RiskStatus.MEDIUM, "Mixed waste increasing.", LocalDateTime.now().minusDays(2));
        createAreaWithData("Baner", "Ward 5", 30, 20, 10, 1, 3, RiskStatus.HIGH, "Hazardous waste reported.", LocalDateTime.now().minusDays(4));

        riskEngine.recalculateAll();
        priorityEngine.generatePriorities();

        return "Demo data seeded successfully (5 areas, simulated metrics). Use 'password' for all accounts.";
    }

    private void createAreaWithData(String name, String ward, int totalScans, int correctScans, int mixed, int haz, int reports, RiskStatus status, String explain, LocalDateTime coll) {
        Area a = areaRepository.save(Area.builder().name(name).ward(ward).city("Pune").build());
        double segScore = totalScans > 0 ? (double) correctScans / totalScans * 100 : 0;
        metricsRepository.save(AreaMetrics.builder()
                .area(a)
                .totalScans(totalScans)
                .correctSegregationCount(correctScans)
                .mixedWasteCount(mixed)
                .hazardousCount(haz)
                .totalReports(reports)
                .openReports(reports)
                .segregationScore(segScore)
                .riskScore(status == RiskStatus.CRITICAL ? 85.0 : status == RiskStatus.HIGH ? 65.0 : status == RiskStatus.MEDIUM ? 45.0 : 15.0)
                .riskStatus(status)
                .riskExplanation(explain)
                .lastCollection(coll)
                .build());
    }

    @Transactional
    public void resetDemoData() {
        taskRepository.deleteAll();
        reportRepository.deleteAll();
        scanRepository.deleteAll();
        metricsRepository.deleteAll();
        areaRepository.deleteAll();
        userRepository.deleteAll();
    }
}
