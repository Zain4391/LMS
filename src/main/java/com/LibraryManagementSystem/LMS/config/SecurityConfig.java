package com.LibraryManagementSystem.LMS.config;

import com.LibraryManagementSystem.LMS.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                
                // Librarian management - Admin only
                .requestMatchers("/api/librarians/**").hasRole("ADMIN")
                
                // Admin and Staff can read, only Admin can create/update/delete
                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                
                // All authenticated users can read, only Admin/Staff can modify
                .requestMatchers(HttpMethod.GET, "/api/books/**").authenticated()
                .requestMatchers("/api/books/**").hasAnyRole("ADMIN", "STAFF")
                
                // All authenticated users can read, only Admin/Staff can modify
                .requestMatchers(HttpMethod.GET, "/api/authors/**").authenticated()
                .requestMatchers("/api/authors/**").hasAnyRole("ADMIN", "STAFF")
                
                // Users can borrow, Staff can manage
                .requestMatchers(HttpMethod.POST, "/api/borrowing/**").hasAnyRole("USER", "ADMIN", "STAFF")
                .requestMatchers("/api/borrowing/**").hasAnyRole("ADMIN", "STAFF")
                
                // All other requests must be authenticated
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}