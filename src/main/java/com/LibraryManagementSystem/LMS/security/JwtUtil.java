package com.LibraryManagementSystem.LMS.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.LibraryManagementSystem.LMS.config.JwtConfig;


@Component
public class JwtUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
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
    
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }
    
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            logger.warn("Token is expired");
            return true;
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            
            // Explicit expiration check
            if (claims.getExpiration().before(new Date())) {
                logger.warn("Token has expired");
                throw new ExpiredJwtException(null, claims, "Token has expired");
            }
            
            // Verify subject exists
            if (claims.getSubject() == null || claims.getSubject().isEmpty()) {
                logger.error("Token subject is missing");
                return false;
            }
            
            // Verify role exists
            String role = claims.get("role", String.class);
            if (role == null || role.isEmpty()) {
                logger.error("Token role claim is missing");
                return false;
            }
            
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            throw e; // Re-throw to be caught by exception handler
        } catch (MalformedJwtException e) {
            logger.error("JWT token is malformed: {}", e.getMessage());
            throw e;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("JWT signature validation failed: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            logger.error("JWT validation error: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("JWT token is invalid: {}", e.getMessage());
            return false;
        }
    }
}
