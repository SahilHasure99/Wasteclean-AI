package com.wastewise.repository;

import com.wastewise.entity.PriorityTask;
import com.wastewise.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PriorityTaskRepository extends JpaRepository<PriorityTask, Long> {
    List<PriorityTask> findByStatusOrderByPriorityScoreDesc(TaskStatus status);
    List<PriorityTask> findByAssignedToIdOrderByPriorityScoreDesc(Long collectorId);
    List<PriorityTask> findByAreaIdAndStatusNot(Long areaId, TaskStatus status);
    List<PriorityTask> findAllByOrderByPriorityScoreDesc();
    long countByStatus(TaskStatus status);
}
