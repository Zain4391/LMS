package com.LibraryManagementSystem.LMS.service.interfaces;

import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.enums.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface BorrowedService {
    
    // Core CRUD methods
    Borrowed create(Borrowed borrowed);
    
    Borrowed getById(Long id);
    
    List<Borrowed> getAll();
    
    Page<Borrowed> getAllPaginated(Pageable pageable);
    
    Borrowed update(Long id, Borrowed borrowed);
    
    void delete(Long id);
    
    // Find by User
    List<Borrowed> findByUserId(Long userId);
    
    Page<Borrowed> findByUserId(Long userId, Pageable pageable);
    
    // Find by Status
    List<Borrowed> findByStatus(BorrowStatus status);
    
    Page<Borrowed> findByStatus(BorrowStatus status, Pageable pageable);
    
    // Find overdue borrows
    List<Borrowed> findOverdueBorrows();
    
    Page<Borrowed> findOverdueBorrows(Pageable pageable);
    
    // Business logic methods
    Borrowed returnBook(Long borrowedId, LocalDate returnDate);
}
