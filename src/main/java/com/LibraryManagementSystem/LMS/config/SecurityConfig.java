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
                // Public endpoints - No authentication required
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // User Management - Mixed permissions
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // Registration handled by /api/auth/register (this is redundant but safe)
                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.POST, "/api/users/*/change-password").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                
                // Librarian Management - Admin only
                .requestMatchers("/api/librarians/**").hasRole("ADMIN")
                
                // Book Management - Read: All authenticated, Write: Staff/Admin
                .requestMatchers(HttpMethod.GET, "/api/books/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/books/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/books/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PATCH, "/api/books/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasAnyRole("ADMIN", "STAFF")
                
                // Author Management - Read: All authenticated, Write: Staff/Admin
                .requestMatchers(HttpMethod.GET, "/api/authors/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/authors/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/authors/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/authors/**").hasAnyRole("ADMIN", "STAFF")
                
                // Genre Management - Read: All authenticated, Write: Staff/Admin
                .requestMatchers(HttpMethod.GET, "/api/genres/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/genres/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/genres/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/genres/**").hasAnyRole("ADMIN", "STAFF")
                
                // Publisher Management - Read: All authenticated, Write: Staff/Admin
                .requestMatchers(HttpMethod.GET, "/api/publishers/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/publishers/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/publishers/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/publishers/**").hasAnyRole("ADMIN", "STAFF")
                
                // Book Copy Management - Read: All authenticated, Write: Staff/Admin
                .requestMatchers(HttpMethod.GET, "/api/book-copies/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/book-copies/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/book-copies/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PATCH, "/api/book-copies/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/book-copies/**").hasAnyRole("ADMIN", "STAFF")
                
                // Borrowing Management - Users can borrow/return, Staff/Admin can manage all
                .requestMatchers(HttpMethod.GET, "/api/borrowed/**").hasAnyRole("USER", "ADMIN", "STAFF")
                .requestMatchers(HttpMethod.POST, "/api/borrowed/**").hasAnyRole("USER", "ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/borrowed/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PATCH, "/api/borrowed/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/borrowed/**").hasAnyRole("ADMIN", "STAFF")
                
                // Fine Management - Read: Users can see their fines, Write: Staff/Admin only
                .requestMatchers(HttpMethod.GET, "/api/fines/**").hasAnyRole("USER", "ADMIN", "STAFF")
                .requestMatchers(HttpMethod.POST, "/api/fines/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/fines/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PATCH, "/api/fines/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/fines/**").hasAnyRole("ADMIN", "STAFF")
                
                // Payment Management - Users can pay their fines, Staff/Admin can manage all
                .requestMatchers(HttpMethod.GET, "/api/payments/**").hasAnyRole("USER", "ADMIN", "STAFF")
                .requestMatchers(HttpMethod.POST, "/api/payments/**").hasAnyRole("USER", "ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/payments/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PATCH, "/api/payments/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/payments/**").hasAnyRole("ADMIN", "STAFF")
                
                // All other requests must be authenticated
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}