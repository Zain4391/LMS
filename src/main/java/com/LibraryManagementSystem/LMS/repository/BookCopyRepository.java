package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.entity.Book;
import com.LibraryManagementSystem.LMS.enums.BookCopyStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    
    // Find book copy by barcode
    Optional<BookCopy> findByBarcode(String barcode);
    
    // Find all copies of a specific book
    List<BookCopy> findByBook(Book book);
    Page<BookCopy> findByBook(Book book, Pageable pageable);
    
    // Find copies by book ID
    List<BookCopy> findByBookId(Long bookId);
    Page<BookCopy> findByBookId(Long bookId, Pageable pageable);
    
    // Find copies by status
    List<BookCopy> findByStatus(BookCopyStatus status);
    Page<BookCopy> findByStatus(BookCopyStatus status, Pageable pageable);
    
    // Find copies by book and status
    List<BookCopy> findByBookAndStatus(Book book, BookCopyStatus status);
    Page<BookCopy> findByBookAndStatus(Book book, BookCopyStatus status, Pageable pageable);
    
    // Find copies by book ID and status
    List<BookCopy> findByBookIdAndStatus(Long bookId, BookCopyStatus status);
    Page<BookCopy> findByBookIdAndStatus(Long bookId, BookCopyStatus status, Pageable pageable);
    
    // Find available copies of a book
    @Query("SELECT bc FROM BookCopy bc WHERE bc.book.id = :bookId AND bc.status = com.LibraryManagementSystem.LMS.enums.BookCopyStatus.AVAILABLE")
    List<BookCopy> findAvailableCopiesByBookId(@Param("bookId") Long bookId);
    
    @Query("SELECT bc FROM BookCopy bc WHERE bc.book.id = :bookId AND bc.status = com.LibraryManagementSystem.LMS.enums.BookCopyStatus.AVAILABLE")
    Page<BookCopy> findAvailableCopiesByBookId(@Param("bookId") Long bookId, Pageable pageable);
    
    // Find copies by location
    List<BookCopy> findByLocation(String location);
    Page<BookCopy> findByLocation(String location, Pageable pageable);
    
    // Find copies by condition
    List<BookCopy> findByCondition(String condition);
    Page<BookCopy> findByCondition(String condition, Pageable pageable);
    
    // Find copies by location and status
    List<BookCopy> findByLocationAndStatus(String location, BookCopyStatus status);
    Page<BookCopy> findByLocationAndStatus(String location, BookCopyStatus status, Pageable pageable);
    
    // Check if barcode exists
    boolean existsByBarcode(String barcode);
    
    // Count copies by book
    long countByBook(Book book);
    
    // Count copies by book ID
    long countByBookId(Long bookId);
    
    // Count available copies by book
    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.book.id = :bookId AND bc.status = com.LibraryManagementSystem.LMS.enums.BookCopyStatus.AVAILABLE")
    long countAvailableCopiesByBookId(@Param("bookId") Long bookId);
    
    // Count copies by status
    long countByStatus(BookCopyStatus status);
}
