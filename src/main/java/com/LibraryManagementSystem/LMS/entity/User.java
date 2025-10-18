package com.LibraryManagementSystem.LMS.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.LibraryManagementSystem.LMS.enums.Status;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private static final transient PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // excluded from JSON responses
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(name = "membership_date", nullable = false)
    private LocalDate membershipDate;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE; // default status for users

    @PrePersist
    @PreUpdate
    private void beforeSave() {
        if (this.password == null || this.password.isBlank()) return;
        if (!(this.password.startsWith("$2a$") || this.password.startsWith("$2b$") || this.password.startsWith("$2y$"))) {
            this.password = passwordEncoder.encode(this.password);
        }

        if(this.status == null) {
            this.status = Status.ACTIVE;
        }
    }

}   
