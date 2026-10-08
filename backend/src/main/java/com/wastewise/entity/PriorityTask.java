package com.wastewise.entity;

import com.wastewise.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "priority_tasks", indexes = {
    @Index(name = "idx_task_status_priority", columnList = "status,priority_score")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PriorityTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    private Integer priorityScore;
    private String recommendedAction;

    @Column(columnDefinition = "TEXT")
    private String justification;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

    private String beforeImageUrl;
    private String afterImageUrl;
    private String verificationStatus;
    private Double verificationConfidence;

    @Column(columnDefinition = "TEXT")
    private String verificationExplanation;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDateTime verifiedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
