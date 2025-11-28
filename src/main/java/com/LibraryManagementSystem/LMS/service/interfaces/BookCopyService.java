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

    // Find by Book relationship
    List<BookCopy> findByBookId(Long bookId);
    Page<BookCopy> findByBookId(Long bookId, Pageable pageable);
    
    // Find by status
    List<BookCopy> findByStatus(BookCopyStatus status);
    Page<BookCopy> findByStatus(BookCopyStatus status, Pageable pageable);
}
