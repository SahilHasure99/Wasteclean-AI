package com.wastewise.repository;

import com.wastewise.entity.WasteReport;
import com.wastewise.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WasteReportRepository extends JpaRepository<WasteReport, Long> {
    List<WasteReport> findByAreaIdAndStatus(Long areaId, ReportStatus status);
    List<WasteReport> findAllByOrderByCreatedAtDesc();
    List<WasteReport> findTop20ByOrderByCreatedAtDesc();
    long countByAreaId(Long areaId);
    long countByAreaIdAndStatus(Long areaId, ReportStatus status);
    long countByStatus(ReportStatus status);
}
