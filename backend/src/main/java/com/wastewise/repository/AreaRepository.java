package com.wastewise.repository;

import com.wastewise.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByName(String name);
    boolean existsByName(String name);
}
