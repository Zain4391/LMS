package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Librarian;
import com.LibraryManagementSystem.LMS.enums.Role;
import com.LibraryManagementSystem.LMS.enums.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
    
    // Find librarian by email
    Optional<Librarian> findByEmail(String email);
    
    // Find librarians by role
    List<Librarian> findByRole(Role role);
    
    // Find librarians by status
    List<Librarian> findByStatus(Status status);
    
    // Find librarians by name containing (case-insensitive)
    List<Librarian> findByNameContainingIgnoreCase(String name);
    
    // Find active librarians
    List<Librarian> findByStatusAndRole(Status status, Role role);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if phone number exists
    boolean existsByPhoneNumber(String phoneNumber);
    
    // Count librarians by role
    long countByRole(Role role);
    
    // Count librarians by status
    long countByStatus(Status status);
}
