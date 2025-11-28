package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Librarian;
import com.LibraryManagementSystem.LMS.enums.Role;
import com.LibraryManagementSystem.LMS.enums.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
    
    Optional<Librarian> findByEmail(String email);
    
    List<Librarian> findByRole(Role role);
    Page<Librarian> findByRole(Role role, Pageable pageable);
    
    List<Librarian> findByStatus(Status status);
    Page<Librarian> findByStatus(Status status, Pageable pageable);
    
    List<Librarian> findByNameContainingIgnoreCase(String name);
    Page<Librarian> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    List<Librarian> findByStatusAndRole(Status status, Role role);
    Page<Librarian> findByStatusAndRole(Status status, Role role, Pageable pageable);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    long countByRole(Role role);
    
    long countByStatus(Status status);
}
