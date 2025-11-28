package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.entity.Book;
import com.LibraryManagementSystem.LMS.enums.BookCopyStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    
    
    Optional<BookCopy> findByBarcode(String barcode);
    
    List<BookCopy> findByBook(Book book);
    Page<BookCopy> findByBook(Book book, Pageable pageable);
    
    List<BookCopy> findByBookId(Long bookId);
    Page<BookCopy> findByBookId(Long bookId, Pageable pageable);
    
    List<BookCopy> findByStatus(BookCopyStatus status);
    Page<BookCopy> findByStatus(BookCopyStatus status, Pageable pageable);
    
    List<BookCopy> findByBookAndStatus(Book book, BookCopyStatus status);
    Page<BookCopy> findByBookAndStatus(Book book, BookCopyStatus status, Pageable pageable);
    
    List<BookCopy> findByBookIdAndStatus(Long bookId, BookCopyStatus status);
    Page<BookCopy> findByBookIdAndStatus(Long bookId, BookCopyStatus status, Pageable pageable);
    
    List<BookCopy> findByLocation(String location);
    Page<BookCopy> findByLocation(String location, Pageable pageable);
    
    List<BookCopy> findByCondition(String condition);
    Page<BookCopy> findByCondition(String condition, Pageable pageable);
    
    List<BookCopy> findByLocationAndStatus(String location, BookCopyStatus status);
    Page<BookCopy> findByLocationAndStatus(String location, BookCopyStatus status, Pageable pageable);
    
    boolean existsByBarcode(String barcode);
    
}
