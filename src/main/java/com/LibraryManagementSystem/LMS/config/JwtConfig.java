package com.LibraryManagementSystem.LMS.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JwtConfig {

    @Value("${jwt.secret:your-secret-key-change-in-production-must-be-at-least-256-bits}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 hours in ms
    private Long expiration;

    public String getSecret() {
        return secret;
    }

    public Long getExpiration() {
        return expiration;
    }
}
