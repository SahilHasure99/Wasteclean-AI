package com.wastewise.controller;

import com.wastewise.dto.waste.ScanResponse;
import com.wastewise.security.JwtUserPrincipal;
import com.wastewise.service.WasteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/waste")
@RequiredArgsConstructor
public class WasteController {
    
    private final WasteService wasteService;

    @PostMapping("/analyze")
    public ResponseEntity<ScanResponse> analyzeWaste(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "areaId", required = false) Long areaId) throws IOException {
        return ResponseEntity.ok(wasteService.analyzeWaste(principal.userId(), image, areaId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ScanResponse>> getHistory(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(wasteService.getHistory(principal.userId(), page, size));
    }

    @GetMapping("/scan/{id}")
    public ResponseEntity<ScanResponse> getScan(@PathVariable Long id) {
        return ResponseEntity.ok(wasteService.getScan(id));
    }
}
