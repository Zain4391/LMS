package com.LibraryManagementSystem.LMS.repository;

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
    
    // Find books by status
    List<Book> findByStatus(BookStatus status);
    
    // Find books by publisher
    List<Book> findByPublisher(Publisher publisher);
    
    // Find books by language
    List<Book> findByLanguage(String language);
    
    // Find books by author name
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    List<Book> findByAuthorName(@Param("authorName") String authorName);
    
    // Find books by genre name
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE LOWER(g.name) = LOWER(:genreName)")
    List<Book> findByGenreName(@Param("genreName") String genreName);
    
    // Find books by multiple criteria
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN b.authors a WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooks(@Param("keyword") String keyword);
    
    // Check if ISBN exists
    boolean existsByIsbn(String isbn);
    
    // Count books by status
    long countByStatus(BookStatus status);
}
