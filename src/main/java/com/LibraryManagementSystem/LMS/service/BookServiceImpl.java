package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.entity.Book;
import com.LibraryManagementSystem.LMS.entity.Publisher;
import com.LibraryManagementSystem.LMS.enums.BookStatus;
import com.LibraryManagementSystem.LMS.repository.BookRepository;
import com.LibraryManagementSystem.LMS.repository.PublisherRepository;
import com.LibraryManagementSystem.LMS.service.interfaces.BookService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BookServiceImpl(BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }
    
    @Override
    public Book create(Book book) {
        // Validate ISBN uniqueness before creating
        if (book.getIsbn() != null && bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        return bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return bookRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Book> getAllPaginated(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }
    
    @Override
    public Book update(Long id, Book book) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        
        // Validate ISBN uniqueness if being changed
        if (book.getIsbn() != null && !book.getIsbn().equals(existingBook.getIsbn())) {
            if (bookRepository.existsByIsbn(book.getIsbn())) {
                throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
            }
        }
        
        existingBook.setIsbn(book.getIsbn());
        existingBook.setTitle(book.getTitle());
        existingBook.setDescription(book.getDescription());
        existingBook.setPublicationDate(book.getPublicationDate());
        existingBook.setLanguage(book.getLanguage());
        existingBook.setPageCount(book.getPageCount());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setAuthors(book.getAuthors());
        existingBook.setGenres(book.getGenres());
        existingBook.setStatus(book.getStatus());
        return bookRepository.save(existingBook);
    }
    
    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        bookRepository.delete(book);
    }
    
    // Additional search and filter methods
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Book> findByTitleContaining(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByTitleContaining(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Book> findByStatus(BookStatus status) {
        return bookRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByStatus(BookStatus status, Pageable pageable) {
        return bookRepository.findByStatus(status, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Book> findByPublisher(Publisher publisher) {
        return bookRepository.findByPublisher(publisher);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Book> findByPublisherId(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "id", publisherId));
        return bookRepository.findByPublisher(publisher);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByPublisherId(Long publisherId, Pageable pageable) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher", "id", publisherId));
        return bookRepository.findByPublisher(publisher, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Book> findByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByLanguage(String language, Pageable pageable) {
        return bookRepository.findByLanguage(language, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Book> findByAuthorName(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByAuthorName(String authorName, Pageable pageable) {
        return bookRepository.findByAuthorName(authorName, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Book> findByGenreName(String genreName) {
        return bookRepository.findByGenreName(genreName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByGenreName(String genreName, Pageable pageable) {
        return bookRepository.findByGenreName(genreName, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchBooks(keyword);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Book> searchBooks(String keyword, Pageable pageable) {
        return bookRepository.searchBooks(keyword, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(BookStatus status) {
        return bookRepository.countByStatus(status);
    }
}
