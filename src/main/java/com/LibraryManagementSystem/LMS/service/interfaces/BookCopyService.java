package com.LibraryManagementSystem.LMS.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.enums.BookCopyStatus;

public interface BookCopyService {
    
    // Core CRUD methods
    BookCopy create(BookCopy bookCopy);
    BookCopy getById(Long id);
    List<BookCopy> getAll();
    Page<BookCopy> getAllPaginated(Pageable pageable);
    BookCopy update(Long id, BookCopy bookCopy);
    void delete(Long id);

    // Find by barcode (unique identifier)
    BookCopy findByBarcode(String barcode);
    boolean existsByBarcode(String barcode);

    // Find by Book relationship
    List<BookCopy> findByBookId(Long bookId);
    Page<BookCopy> findByBookId(Long bookId, Pageable pageable);
    
    // Find by status
    List<BookCopy> findByStatus(BookCopyStatus status);
    Page<BookCopy> findByStatus(BookCopyStatus status, Pageable pageable);

    // Find by Book and Status (combined filters)
    List<BookCopy> findByBookIdAndStatus(Long bookId, BookCopyStatus status);
    Page<BookCopy> findByBookIdAndStatus(Long bookId, BookCopyStatus status, Pageable pageable);
    
    // Find available copies of a specific book
    List<BookCopy> findAvailableCopiesByBookId(Long bookId);
    Page<BookCopy> findAvailableCopiesByBookId(Long bookId, Pageable pageable);
    
    // Find by location
    List<BookCopy> findByLocation(String location);
    Page<BookCopy> findByLocation(String location, Pageable pageable);
    
    // Find by condition
    List<BookCopy> findByCondition(String condition);
    Page<BookCopy> findByCondition(String condition, Pageable pageable);
    
    // Find by location and status
    List<BookCopy> findByLocationAndStatus(String location, BookCopyStatus status);
    Page<BookCopy> findByLocationAndStatus(String location, BookCopyStatus status, Pageable pageable);
    
    // Count methods
    long countByBookId(Long bookId);
    long countByStatus(BookCopyStatus status);
    long countAvailableCopiesByBookId(Long bookId);
}
