package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.enums.BookCopyStatus;
import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;
import com.LibraryManagementSystem.LMS.repository.BookCopyRepository;
import com.LibraryManagementSystem.LMS.repository.BookRepository;
import com.LibraryManagementSystem.LMS.service.interfaces.BookCopyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookCopyImpl implements BookCopyService {
    
    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;
    
    public BookCopyImpl(BookCopyRepository bookCopyRepository, BookRepository bookRepository) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
    }
    
    // Core CRUD methods
    
    @Override
    public BookCopy create(BookCopy bookCopy) {
        // Validate barcode uniqueness
        if (bookCopy.getBarcode() != null && bookCopyRepository.existsByBarcode(bookCopy.getBarcode())) {
            throw new IllegalArgumentException("Book copy with barcode " + bookCopy.getBarcode() + " already exists");
        }
        
        // Validate book exists
        if (bookCopy.getBook() != null && bookCopy.getBook().getId() != null) {
            bookRepository.findById(bookCopy.getBook().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookCopy.getBook().getId()));
        }
        
        return bookCopyRepository.save(bookCopy);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BookCopy getById(Long id) {
        return bookCopyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BookCopy", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BookCopy> getAll() {
        return bookCopyRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookCopy> getAllPaginated(Pageable pageable) {
        return bookCopyRepository.findAll(pageable);
    }
    
    @Override
    public BookCopy update(Long id, BookCopy bookCopy) {
        BookCopy existingBookCopy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BookCopy", "id", id));
        
        // Validate barcode uniqueness if changed
        if (bookCopy.getBarcode() != null && !bookCopy.getBarcode().equals(existingBookCopy.getBarcode())) {
            if (bookCopyRepository.existsByBarcode(bookCopy.getBarcode())) {
                throw new IllegalArgumentException("Book copy with barcode " + bookCopy.getBarcode() + " already exists");
            }
        }
        
        // Update fields
        existingBookCopy.setBarcode(bookCopy.getBarcode());
        existingBookCopy.setCondition(bookCopy.getCondition());
        existingBookCopy.setStatus(bookCopy.getStatus());
        existingBookCopy.setAcquisitionDate(bookCopy.getAcquisitionDate());
        existingBookCopy.setLocation(bookCopy.getLocation());
        existingBookCopy.setBook(bookCopy.getBook());
        
        return bookCopyRepository.save(existingBookCopy);
    }
    
    @Override
    public void delete(Long id) {
        BookCopy bookCopy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BookCopy", "id", id));
        bookCopyRepository.delete(bookCopy);
    }
    
    // Barcode operations
    
    @Override
    @Transactional(readOnly = true)
    public BookCopy findByBarcode(String barcode) {
        return bookCopyRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException("BookCopy", "barcode", barcode));
    }
    
    // Find by Book relationship
    
    @Override
    @Transactional(readOnly = true)
    public List<BookCopy> findByBookId(Long bookId) {
        return bookCopyRepository.findByBookId(bookId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookCopy> findByBookId(Long bookId, Pageable pageable) {
        return bookCopyRepository.findByBookId(bookId, pageable);
    }
    
    // Find by status
    
    @Override
    @Transactional(readOnly = true)
    public List<BookCopy> findByStatus(BookCopyStatus status) {
        return bookCopyRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookCopy> findByStatus(BookCopyStatus status, Pageable pageable) {
        return bookCopyRepository.findByStatus(status, pageable);
    }
}
