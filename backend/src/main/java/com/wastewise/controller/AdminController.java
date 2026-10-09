package com.wastewise.controller;

import com.wastewise.dto.admin.AdminDashboardResponse;
import com.wastewise.dto.admin.HotspotResponse;
import com.wastewise.dto.admin.PriorityResponse;
import com.wastewise.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboard());
    }

    @GetMapping("/hotspots")
    public ResponseEntity<List<HotspotResponse>> getHotspots() {
        return ResponseEntity.ok(adminService.getHotspots());
    }

    @GetMapping("/priorities")
    public ResponseEntity<List<PriorityResponse>> getPriorities() {
        return ResponseEntity.ok(adminService.getPriorities());
    }
}
