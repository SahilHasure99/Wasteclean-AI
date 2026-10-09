package com.wastewise.repository;

import com.wastewise.entity.WasteScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WasteScanRepository extends JpaRepository<WasteScan, Long> {
    Page<WasteScan> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    List<WasteScan> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<WasteScan> findByAreaIdOrderByCreatedAtDesc(Long areaId);
    List<WasteScan> findTop10ByOrderByCreatedAtDesc();
    long countByUserId(Long userId);
    long countByUserIdAndCorrectlySegregated(Long userId, Boolean correctlySegregated);
    long countByAreaId(Long areaId);
    long countByAreaIdAndCorrectlySegregated(Long areaId, Boolean correctlySegregated);
    long countByCategory(String category);

    @Query("SELECT s.category, COUNT(s) FROM WasteScan s GROUP BY s.category")
    List<Object[]> countByCategories();
}
