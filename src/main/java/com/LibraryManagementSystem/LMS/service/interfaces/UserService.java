package com.LibraryManagementSystem.LMS.service.interfaces;

import com.LibraryManagementSystem.LMS.entity.User;
import com.LibraryManagementSystem.LMS.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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
    
    // Find user by phone number
    User findByPhoneNumber(String phoneNumber);
    
    // Find users by membership date range
    List<User> findByMembershipDateBetween(LocalDate startDate, LocalDate endDate);
    
    Page<User> findByMembershipDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // Find users with overdue books
    List<User> findUsersWithOverdueBooks();
    
    Page<User> findUsersWithOverdueBooks(Pageable pageable);
    
    // Find users with pending fines
    List<User> findUsersWithPendingFines();
    
    Page<User> findUsersWithPendingFines(Pageable pageable);
    
    // Business logic methods
    User activateUser(Long userId);
    
    User deactivateUser(Long userId);
    
    User suspendUser(Long userId);
    
    User changePassword(Long userId, String oldPassword, String newPassword);
    
    // Validation methods
    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    long countByStatus(Status status);
    
    boolean canUserBorrow(Long userId);
}
