package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Publisher;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    
    Optional<Publisher> findByName(String name);
    
    List<Publisher> findByNameContainingIgnoreCase(String name);
    
    List<Publisher> findByCountry(String country);
    
    // Find publishers by country containing (case-insensitive)
    List<Publisher> findByCountryContainingIgnoreCase(String country);
    
    Optional<Publisher> findByEmail(String email);
    
    boolean existsByName(String name);
    
    boolean existsByEmail(String email);
}
