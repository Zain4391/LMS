package com.LibraryManagementSystem.LMS.service.interfaces;

import com.LibraryManagementSystem.LMS.entity.Book;
import com.LibraryManagementSystem.LMS.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface BookService {
    
    Book create(Book book);
    
    Book getById(Long id);
    
    List<Book> getAll();
    
    Page<Book> getAllPaginated(Pageable pageable);
    
    Book update(Long id, Book book);
    
    void delete(Long id);
    
    // Additional search and filter methods (with pagination)
    Optional<Book> findByIsbn(String isbn);
    
    List<Book> findByTitleContaining(String title);
    
    Page<Book> findByTitleContaining(String title, Pageable pageable);
    
    List<Book> findByStatus(BookStatus status);
    
    Page<Book> findByStatus(BookStatus status, Pageable pageable);
    
    List<Book> findByPublisherId(Long publisherId);
    
    Page<Book> findByPublisherId(Long publisherId, Pageable pageable);
    
    List<Book> findByAuthorName(String authorName);
    
    Page<Book> findByAuthorName(String authorName, Pageable pageable);
}
