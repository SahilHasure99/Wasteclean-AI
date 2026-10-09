package com.wastewise.controller;

import com.wastewise.dto.citizen.CitizenDashboardResponse;
import com.wastewise.security.JwtUserPrincipal;
import com.wastewise.service.CitizenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citizen")
@RequiredArgsConstructor
public class CitizenController {

    private final CitizenService citizenService;

    @GetMapping("/dashboard")
    public ResponseEntity<CitizenDashboardResponse> getDashboard(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ResponseEntity.ok(citizenService.getDashboard(principal.userId()));
    }
}
