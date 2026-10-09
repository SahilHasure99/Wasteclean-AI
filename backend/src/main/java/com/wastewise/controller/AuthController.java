package com.wastewise.controller;

import com.wastewise.dto.auth.AuthResponse;
import com.wastewise.dto.auth.LoginRequest;
import com.wastewise.dto.auth.RegisterRequest;
import com.wastewise.entity.User;
import com.wastewise.security.JwtUserPrincipal;
import com.wastewise.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMe(@AuthenticationPrincipal JwtUserPrincipal principal) {
        User user = authService.getUserById(principal.userId());
        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "email", user.getEmail(),
            "role", user.getRole().name(),
            "points", user.getPoints(),
            "streak", user.getStreak(),
            "badge", user.getBadge() != null ? user.getBadge() : ""
        ));
    }
}
