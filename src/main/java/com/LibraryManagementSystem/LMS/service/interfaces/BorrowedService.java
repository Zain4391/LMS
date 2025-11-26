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
    
    // Find by BookCopy
    List<Borrowed> findByBookCopyId(Long bookCopyId);
    
    Page<Borrowed> findByBookCopyId(Long bookCopyId, Pageable pageable);
    
    // Find by Status
    List<Borrowed> findByStatus(BorrowStatus status);
    
    Page<Borrowed> findByStatus(BorrowStatus status, Pageable pageable);
    
    // Find by User and Status
    List<Borrowed> findByUserIdAndStatus(Long userId, BorrowStatus status);
    
    Page<Borrowed> findByUserIdAndStatus(Long userId, BorrowStatus status, Pageable pageable);
    
    // Find active (borrowed/overdue) borrows by user
    List<Borrowed> findActiveBorrowsByUserId(Long userId);
    
    Page<Borrowed> findActiveBorrowsByUserId(Long userId, Pageable pageable);
    
    // Find overdue borrows
    List<Borrowed> findOverdueBorrows();
    
    Page<Borrowed> findOverdueBorrows(Pageable pageable);
    
    // Find overdue borrows by user
    List<Borrowed> findOverdueBorrowsByUserId(Long userId);
    
    Page<Borrowed> findOverdueBorrowsByUserId(Long userId, Pageable pageable);
    
    // Find by date ranges
    List<Borrowed> findByBorrowDateBetween(LocalDate startDate, LocalDate endDate);
    
    Page<Borrowed> findByBorrowDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    List<Borrowed> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    
    Page<Borrowed> findByDueDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    List<Borrowed> findByReturnDateBetween(LocalDate startDate, LocalDate endDate);
    
    Page<Borrowed> findByReturnDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // Business logic methods
    Borrowed returnBook(Long borrowedId, LocalDate returnDate);
    
    void markOverdue();
    
    boolean isBookCopyAvailable(Long bookCopyId);
    
    boolean hasUserExceededBorrowLimit(Long userId, int limit);
    
    int countActiveBorrowsByUserId(Long userId);
}
