package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LibraryManagementSystem.LMS.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    
}
