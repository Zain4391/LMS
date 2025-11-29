package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.dto.LibrarianPatchDTO;
import com.LibraryManagementSystem.LMS.entity.Librarian;
import com.LibraryManagementSystem.LMS.enums.Role;
import com.LibraryManagementSystem.LMS.enums.Status;
import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;
import com.LibraryManagementSystem.LMS.repository.LibrarianRepository;
import com.LibraryManagementSystem.LMS.service.interfaces.LibrarianService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LibrarianServiceImpl implements LibrarianService {
    
    private final LibrarianRepository librarianRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LibrarianServiceImpl(LibrarianRepository librarianRepository) {
        this.librarianRepository = librarianRepository;
    }
    
    // Core CRUD methods
    
    @Override
    public Librarian create(Librarian librarian) {
        // Validate email uniqueness
        if (librarian.getEmail() != null && librarianRepository.existsByEmail(librarian.getEmail())) {
            throw new IllegalStateException("Librarian with email " + librarian.getEmail() + " already exists");
        }
        
        // Validate phone number uniqueness
        if (librarian.getPhoneNumber() != null && librarianRepository.existsByPhoneNumber(librarian.getPhoneNumber())) {
            throw new IllegalStateException("Librarian with phone number " + librarian.getPhoneNumber() + " already exists");
        }
        
        return librarianRepository.save(librarian);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Librarian getById(Long id) {
        return librarianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Librarian", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Librarian> getAll() {
        return librarianRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Librarian> getAllPaginated(Pageable pageable) {
        return librarianRepository.findAll(pageable);
    }
    
    @Override
    public Librarian update(Long id, Librarian librarian) {
        Librarian existingLibrarian = librarianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Librarian", "id", id));
        
        // Validate email uniqueness if changed
        if (librarian.getEmail() != null && !librarian.getEmail().equals(existingLibrarian.getEmail())) {
            if (librarianRepository.existsByEmail(librarian.getEmail())) {
                throw new IllegalStateException("Librarian with email " + librarian.getEmail() + " already exists");
            }
        }
        
        // Validate phone number uniqueness if changed
        if (librarian.getPhoneNumber() != null && !librarian.getPhoneNumber().equals(existingLibrarian.getPhoneNumber())) {
            if (librarianRepository.existsByPhoneNumber(librarian.getPhoneNumber())) {
                throw new IllegalStateException("Librarian with phone number " + librarian.getPhoneNumber() + " already exists");
            }
        }
        
        existingLibrarian.setName(librarian.getName());
        existingLibrarian.setEmail(librarian.getEmail());
        existingLibrarian.setPassword(librarian.getPassword());
        existingLibrarian.setPhoneNumber(librarian.getPhoneNumber());
        existingLibrarian.setAddress(librarian.getAddress());
        existingLibrarian.setRole(librarian.getRole());
        existingLibrarian.setHireDate(librarian.getHireDate());
        existingLibrarian.setStatus(librarian.getStatus());
        
        return librarianRepository.save(existingLibrarian);
    }

    @Override
    public Librarian patchEmail(Long id, LibrarianPatchDTO patchDTO) {
        Librarian existingLibrarian = librarianRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
            if(patchDTO.getEmail() != null) {
                existingLibrarian.setEmail(patchDTO.getEmail());
            }

            return librarianRepository.save(existingLibrarian);
    }
    
    @Override
    public void delete(Long id) {
        Librarian librarian = librarianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Librarian", "id", id));
        librarianRepository.delete(librarian);
    }
    
    // Find librarian by email
    
    @Override
    @Transactional(readOnly = true)
    public Librarian findByEmail(String email) {
        return librarianRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Librarian", "email", email));
    }
    
    // Find librarians by role
    
    @Override
    @Transactional(readOnly = true)
    public List<Librarian> findByRole(Role role) {
        return librarianRepository.findByRole(role);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Librarian> findByRole(Role role, Pageable pageable) {
        return librarianRepository.findByRole(role, pageable);
    }
    
    // Find librarians by status
    
    @Override
    @Transactional(readOnly = true)
    public List<Librarian> findByStatus(Status status) {
        return librarianRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Librarian> findByStatus(Status status, Pageable pageable) {
        return librarianRepository.findByStatus(status, pageable);
    }
    
    // Business logic methods
    
    @Override
    public Librarian changePassword(Long librarianId, String oldPassword, String newPassword) {
        Librarian librarian = librarianRepository.findById(librarianId)
                .orElseThrow(() -> new ResourceNotFoundException("Librarian", "id", librarianId));
        
        // Verify old password
        if (!passwordEncoder.matches(oldPassword, librarian.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        
        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long");
        }
        
        // Set new password (will be encrypted by @PrePersist/@PreUpdate)
        librarian.setPassword(newPassword);
        return librarianRepository.save(librarian);
    }
}
