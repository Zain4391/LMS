package com.LibraryManagementSystem.LMS.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor; // needed by JPA
import lombok.AllArgsConstructor;

@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(length = 500)
    private String description;
}
