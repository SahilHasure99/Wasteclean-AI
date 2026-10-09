package com.wastewise.service;

import com.wastewise.dto.ai.AiAnalysisResponse;
import com.wastewise.dto.waste.ScanResponse;
import com.wastewise.entity.*;
import com.wastewise.exception.BadRequestException;
import com.wastewise.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WasteService {

    private final WasteScanRepository scanRepository;
    private final AreaRepository areaRepository;
    private final AreaMetricsRepository metricsRepository;
    private final UserRepository userRepository;
    private final AiClientService aiClientService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Transactional
    public ScanResponse analyzeWaste(Long userId, MultipartFile image, Long areaId) throws IOException {
        // 1. Save image file
        String imageUrl = saveImage(image);

        // 2. Call AI service
        AiAnalysisResponse ai = aiClientService.analyzeWaste(image.getBytes());

        // 3. Get user and optional area
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        Area area = null;
        if (areaId != null) {
            area = areaRepository.findById(areaId).orElse(null);
        }

        // 4. Persist scan
        WasteScan scan = WasteScan.builder()
                .user(user)
                .area(area)
                .imageUrl(imageUrl)
                .category(ai.getCategory())
                .subType(ai.getSubType())
                .confidence(ai.getConfidence())
                .contamination(ai.getContamination())
                .conditionDesc(ai.getConditionDesc())
                .recyclable(ai.getRecyclable())
                .disposalCategory(ai.getDisposalCategory())
                .recommendation(ai.getRecommendation())
                .explanation(ai.getExplanation())
                .impactScore(ai.getImpactScore())
                .impactReason(ai.getImpactReason())
                .correctlySegregated(ai.getCorrectlySegregated())
                .build();

        scan = scanRepository.save(scan);

        // 5. Update user points
        user.setPoints(user.getPoints() + 10);
        if (Boolean.TRUE.equals(ai.getCorrectlySegregated())) {
            user.setStreak(user.getStreak() + 1);
            user.setPoints(user.getPoints() + 5);
        } else {
            user.setStreak(0);
        }
        updateBadge(user);
        userRepository.save(user);

        // 6. Update area metrics if area provided
        if (area != null) {
            updateAreaMetrics(area, ai);
        }

        return toScanResponse(scan);
    }

    public List<ScanResponse> getHistory(Long userId, int page, int size) {
        Page<WasteScan> scans = scanRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size));
        return scans.map(this::toScanResponse).getContent();
    }

    public ScanResponse getScan(Long scanId) {
        WasteScan scan = scanRepository.findById(scanId)
                .orElseThrow(() -> new BadRequestException("Scan not found"));
        return toScanResponse(scan);
    }

    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + filename;
    }

    private void updateAreaMetrics(Area area, AiAnalysisResponse ai) {
        AreaMetrics metrics = metricsRepository.findByAreaId(area.getId())
                .orElse(AreaMetrics.builder().area(area).build());

        metrics.setTotalScans(metrics.getTotalScans() + 1);
        if (Boolean.TRUE.equals(ai.getCorrectlySegregated())) {
            metrics.setCorrectSegregationCount(metrics.getCorrectSegregationCount() + 1);
        }
        if ("Mixed Waste".equalsIgnoreCase(ai.getCategory())) {
            metrics.setMixedWasteCount(metrics.getMixedWasteCount() + 1);
        }
        if ("Hazardous".equalsIgnoreCase(ai.getCategory()) || "E-Waste".equalsIgnoreCase(ai.getCategory())) {
            metrics.setHazardousCount(metrics.getHazardousCount() + 1);
        }
        // Recalculate segregation score
        if (metrics.getTotalScans() > 0) {
            metrics.setSegregationScore(
                    (double) metrics.getCorrectSegregationCount() / metrics.getTotalScans() * 100.0);
        }
        metricsRepository.save(metrics);
    }

    private void updateBadge(User user) {
        if (user.getPoints() >= 1000) user.setBadge("🏆 WasteWise Champion");
        else if (user.getPoints() >= 500) user.setBadge("♻\uFE0F Eco Segregator");
        else if (user.getPoints() >= 100) user.setBadge("🌱 Waste Starter");
    }

    private ScanResponse toScanResponse(WasteScan scan) {
        return ScanResponse.builder()
                .id(scan.getId())
                .imageUrl(scan.getImageUrl())
                .category(scan.getCategory())
                .subType(scan.getSubType())
                .confidence(scan.getConfidence())
                .contamination(scan.getContamination())
                .conditionDesc(scan.getConditionDesc())
                .recyclable(scan.getRecyclable())
                .disposalCategory(scan.getDisposalCategory())
                .recommendation(scan.getRecommendation())
                .explanation(scan.getExplanation())
                .impactScore(scan.getImpactScore())
                .impactReason(scan.getImpactReason())
                .correctlySegregated(scan.getCorrectlySegregated())
                .areaName(scan.getArea() != null ? scan.getArea().getName() : null)
                .createdAt(scan.getCreatedAt())
                .build();
    }
}
