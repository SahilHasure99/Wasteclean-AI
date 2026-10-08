package com.wastewise.repository;

import com.wastewise.entity.AreaMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AreaMetricsRepository extends JpaRepository<AreaMetrics, Long> {
    Optional<AreaMetrics> findByAreaId(Long areaId);
    List<AreaMetrics> findAllByOrderByRiskScoreDesc();
}
