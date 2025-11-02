package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    // Find author by name (exact match)
    Optional<Author> findByName(String name);
    
    // Find authors by name containing (partial match)
    List<Author> findByNameContainingIgnoreCase(String name);
    
    // Find authors by nationality
    List<Author> findByNationality(String nationality);
    
    // Find authors by nationality containing
    List<Author> findByNationalityContainingIgnoreCase(String nationality);
    
    // Check if author exists by name
    boolean existsByName(String name);
}
