package com.LibraryManagementSystem.LMS.repository;

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
    
    // Find copies by book ID
    List<BookCopy> findByBookId(Long bookId);
    
    // Find copies by status
    List<BookCopy> findByStatus(BookCopyStatus status);
    
    // Find copies by book and status
    List<BookCopy> findByBookAndStatus(Book book, BookCopyStatus status);
    
    // Find available copies of a book
    @Query("SELECT bc FROM BookCopy bc WHERE bc.book.id = :bookId AND bc.status = 'AVAILABLE'")
    List<BookCopy> findAvailableCopiesByBookId(@Param("bookId") Long bookId);
    
    // Find copies by location
    List<BookCopy> findByLocation(String location);
    
    // Find copies by condition
    List<BookCopy> findByCondition(String condition);
    
    // Check if barcode exists
    boolean existsByBarcode(String barcode);
    
    // Count copies by book
    long countByBook(Book book);
    
    // Count available copies by book
    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.book.id = :bookId AND bc.status = 'AVAILABLE'")
    long countAvailableCopiesByBookId(@Param("bookId") Long bookId);
}
