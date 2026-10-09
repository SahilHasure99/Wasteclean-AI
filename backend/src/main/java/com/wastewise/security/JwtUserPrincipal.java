package com.wastewise.security;

/**
 * Lightweight principal stored in SecurityContext after JWT validation.
 * Avoids a DB lookup on every request (unlike UserDetailsService approach).
 */
public record JwtUserPrincipal(Long userId, String email, String role) {
}
