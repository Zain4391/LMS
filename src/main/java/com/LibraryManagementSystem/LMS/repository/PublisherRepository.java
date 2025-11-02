package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Publisher;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    
    // Find publisher by name (exact match)
    Optional<Publisher> findByName(String name);
    
    // Find publishers by name containing (case-insensitive)
    List<Publisher> findByNameContainingIgnoreCase(String name);
    
    // Find publishers by country
    List<Publisher> findByCountry(String country);
    
    // Find publishers by country containing (case-insensitive)
    List<Publisher> findByCountryContainingIgnoreCase(String country);
    
    // Find publisher by email
    Optional<Publisher> findByEmail(String email);
    
    // Check if name exists
    boolean existsByName(String name);
    
    // Check if email exists
    boolean existsByEmail(String email);
}
