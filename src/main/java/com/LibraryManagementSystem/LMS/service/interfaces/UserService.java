package com.LibraryManagementSystem.LMS.service.interfaces;

import com.LibraryManagementSystem.LMS.entity.User;
import com.LibraryManagementSystem.LMS.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    
    // Core CRUD methods
    User create(User user);

    User getById(Long id);

    List<User> getAll();

    Page<User> getAllPaginated(Pageable pageable);

    User update(Long id, User user);

    void delete(Long id);
    
    // Find user by email
    User findByEmail(String email);
    
    // Find users by status
    List<User> findByStatus(Status status);
    
    Page<User> findByStatus(Status status, Pageable pageable);
    
    // Find users by name
    List<User> findByNameContainingIgnoreCase(String name);
    
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Business logic methods
    User changePassword(Long userId, String oldPassword, String newPassword);
}
