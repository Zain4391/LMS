package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Book;
import com.LibraryManagementSystem.LMS.entity.Publisher;
import com.LibraryManagementSystem.LMS.enums.BookStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // Find book by ISBN
    Optional<Book> findByIsbn(String isbn);
    
    // Find books by title containing (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // Find books by status
    List<Book> findByStatus(BookStatus status);
    Page<Book> findByStatus(BookStatus status, Pageable pageable);
    
    // Find books by publisher
    List<Book> findByPublisher(Publisher publisher);
    Page<Book> findByPublisher(Publisher publisher, Pageable pageable);
    
    // Find books by language
    List<Book> findByLanguage(String language);
    Page<Book> findByLanguage(String language, Pageable pageable);
    
    // Find books by author name
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    List<Book> findByAuthorName(@Param("authorName") String authorName);
    
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    Page<Book> findByAuthorName(@Param("authorName") String authorName, Pageable pageable);
    
    // Find books by genre name
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE LOWER(g.name) = LOWER(:genreName)")
    List<Book> findByGenreName(@Param("genreName") String genreName);
    
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE LOWER(g.name) = LOWER(:genreName)")
    Page<Book> findByGenreName(@Param("genreName") String genreName, Pageable pageable);
    
    // Find books by multiple criteria
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN b.authors a WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooks(@Param("keyword") String keyword);
    
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN b.authors a WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);
    
    // Check if ISBN exists
    boolean existsByIsbn(String isbn);
    
    // Count books by status
    long countByStatus(BookStatus status);
}
