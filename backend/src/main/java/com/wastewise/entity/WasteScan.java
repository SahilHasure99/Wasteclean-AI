package com.wastewise.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waste_scans", indexes = {
    @Index(name = "idx_scan_user", columnList = "user_id,created_at"),
    @Index(name = "idx_scan_area", columnList = "area_id,created_at")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class WasteScan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area;

    private String imageUrl;

    @Column(nullable = false)
    private String category;

    private String subType;
    private Double confidence;
    private String contamination;
    private String conditionDesc;
    private Boolean recyclable;
    private String disposalCategory;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    private Integer impactScore;

    @Column(columnDefinition = "TEXT")
    private String impactReason;

    private Boolean correctlySegregated;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
