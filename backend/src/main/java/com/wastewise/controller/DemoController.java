package com.wastewise.controller;

import com.wastewise.service.DemoDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/demo")
@RequiredArgsConstructor
public class DemoController {

    private final DemoDataService demoDataService;

    @PostMapping("/seed")
    public ResponseEntity<Map<String, String>> seedData() {
        return ResponseEntity.ok(Map.of("message", demoDataService.seedDemoData()));
    }

    @DeleteMapping("/reset")
    public ResponseEntity<Map<String, String>> resetData() {
        demoDataService.resetDemoData();
        return ResponseEntity.ok(Map.of("message", "Database hard reset complete."));
    }
}
