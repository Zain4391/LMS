package com.LibraryManagementSystem.LMS.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;
import com.LibraryManagementSystem.LMS.config.JwtConfig;


@Component
public class JwtUtil {
    
    private final SecretKey secretKey;
    private final Long expiration;

    private Claims extractClaims(String token) {
        return Jwts.parser()
               .verifyWith(secretKey)
               .build()
               .parseSignedClaims(token)
               .getPayload();
    }

    public JwtUtil(JwtConfig jwtConfig) {
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
        this.expiration = jwtConfig.getExpiration();
    }
    
    public String generateToken(String email, String role) {
        return Jwts.builder()
               .subject(email)
               .claim("role", role)
               .issuedAt(new Date())
               .expiration(new Date(System.currentTimeMillis() + expiration))
               .signWith(secretKey)
               .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
