package com.wastewise.entity;

import com.wastewise.enums.RiskStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "area_metrics")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AreaMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false, unique = true)
    private Area area;

    @Builder.Default private Integer totalScans = 0;
    @Builder.Default private Integer correctSegregationCount = 0;
    @Builder.Default private Integer mixedWasteCount = 0;
    @Builder.Default private Integer hazardousCount = 0;
    @Builder.Default private Integer totalReports = 0;
    @Builder.Default private Integer openReports = 0;
    @Builder.Default private Double segregationScore = 0.0;
    @Builder.Default private Double riskScore = 0.0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RiskStatus riskStatus = RiskStatus.LOW;

    @Column(columnDefinition = "TEXT")
    private String riskExplanation;

    private LocalDateTime lastCollection;
    private LocalDateTime updatedAt;

    @PrePersist @PreUpdate
    protected void onSave() {
        updatedAt = LocalDateTime.now();
    }
}
