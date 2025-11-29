package com.LibraryManagementSystem.LMS.service.interfaces;

import com.LibraryManagementSystem.LMS.dto.LibrarianPatchDTO;
import com.LibraryManagementSystem.LMS.entity.Librarian;
import com.LibraryManagementSystem.LMS.enums.Role;
import com.LibraryManagementSystem.LMS.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LibrarianService {
    
    // Core CRUD methods
    Librarian create(Librarian librarian);

    Librarian getById(Long id);

    List<Librarian> getAll();

    Page<Librarian> getAllPaginated(Pageable pageable);

    Librarian update(Long id, Librarian librarian);

    Librarian patchEmail(Long id, LibrarianPatchDTO patchDTO);

    void delete(Long id);
    
    // Find librarian by email
    Librarian findByEmail(String email);
    
    // Find librarians by role
    List<Librarian> findByRole(Role role);
    
    Page<Librarian> findByRole(Role role, Pageable pageable);
    
    // Find librarians by status
    List<Librarian> findByStatus(Status status);
    
    Page<Librarian> findByStatus(Status status, Pageable pageable);
    
    // Business logic methods
    Librarian changePassword(Long librarianId, String oldPassword, String newPassword);
}
