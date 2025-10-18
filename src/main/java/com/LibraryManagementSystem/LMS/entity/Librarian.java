package com.LibraryManagementSystem.LMS.entity;


import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.LibraryManagementSystem.LMS.enums.*;


@Entity
@Table(name = "librarians")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Librarian {

    private static final transient PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Role role = Role.STAFF; // default role for librarians

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(length = 20)
    private Status status = Status.ACTIVE; // default status for librarians


    // additional check which runs before persisting to ensure default role
    @PrePersist
    @PreUpdate
    private void beforeSave() {
        if (this.password != null && !this.password.isBlank()) {
            if (!(this.password.startsWith("$2a$") || this.password.startsWith("$2b$") || this.password.startsWith("$2y$"))) {
                this.password = passwordEncoder.encode(this.password);
            }
        }

        if (this.role == null) {
            this.role = Role.STAFF;
        }
        if (this.status == null) {
            this.status = Status.ACTIVE;
        }
    }
}
