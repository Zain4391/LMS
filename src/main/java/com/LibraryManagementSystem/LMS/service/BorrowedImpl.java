package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.enums.BorrowStatus;
import com.LibraryManagementSystem.LMS.enums.BookCopyStatus;
import com.LibraryManagementSystem.LMS.enums.FineStatus;
import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;
import com.LibraryManagementSystem.LMS.repository.BorrowedRepository;
import com.LibraryManagementSystem.LMS.repository.BookCopyRepository;
import com.LibraryManagementSystem.LMS.repository.FineRepository;
import com.LibraryManagementSystem.LMS.repository.UserRepository;
import com.LibraryManagementSystem.LMS.service.interfaces.BorrowedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class BorrowedImpl implements BorrowedService {
    
    private final BorrowedRepository borrowedRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final FineRepository fineRepository;
    
    // Daily fine rate for overdue books (configurable)
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("5.00");
    
    public BorrowedImpl(BorrowedRepository borrowedRepository, 
                        BookCopyRepository bookCopyRepository,
                        UserRepository userRepository,
                        FineRepository fineRepository) {
        this.borrowedRepository = borrowedRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.fineRepository = fineRepository;
    }
    
    // Core CRUD methods
    
    @Override
    public Borrowed create(Borrowed borrowed) {
        // Validate user exists
        if (borrowed.getUser() != null && borrowed.getUser().getId() != null) {
            userRepository.findById(borrowed.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", borrowed.getUser().getId()));
        }
        
        // Validate book copy exists
        if (borrowed.getBookCopy() != null && borrowed.getBookCopy().getId() != null) {
            BookCopy bookCopy = bookCopyRepository.findById(borrowed.getBookCopy().getId())
                .orElseThrow(() -> new ResourceNotFoundException("BookCopy", "id", borrowed.getBookCopy().getId()));
            
            // Check if book copy is available
            if (bookCopy.getStatus() != BookCopyStatus.AVAILABLE) {
                throw new IllegalStateException("Book copy with ID " + bookCopy.getId() + " is not available for borrowing");
            }
            
            // Update book copy status to BORROWED
            bookCopy.setStatus(BookCopyStatus.BORROWED);
            bookCopyRepository.save(bookCopy);
        }
        
        return borrowedRepository.save(borrowed);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Borrowed getById(Long id) {
        return borrowedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> getAll() {
        return borrowedRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> getAllPaginated(Pageable pageable) {
        return borrowedRepository.findAll(pageable);
    }
    
    @Override
    public Borrowed update(Long id, Borrowed borrowed) {
        Borrowed existingBorrowed = borrowedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", id));
        
        // Update fields
        existingBorrowed.setBorrowDate(borrowed.getBorrowDate());
        existingBorrowed.setDueDate(borrowed.getDueDate());
        existingBorrowed.setReturnDate(borrowed.getReturnDate());
        existingBorrowed.setStatus(borrowed.getStatus());
        existingBorrowed.setUser(borrowed.getUser());
        existingBorrowed.setBookCopy(borrowed.getBookCopy());
        
        return borrowedRepository.save(existingBorrowed);
    }
    
    @Override
    public void delete(Long id) {
        Borrowed borrowed = borrowedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", id));
        borrowedRepository.delete(borrowed);
    }
    
    // Find by User
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> findByUserId(Long userId) {
        return borrowedRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> findByUserId(Long userId, Pageable pageable) {
        return borrowedRepository.findByUserId(userId, pageable);
    }
    
    // Find by Status
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> findByStatus(BorrowStatus status) {
        return borrowedRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> findByStatus(BorrowStatus status, Pageable pageable) {
        return borrowedRepository.findByStatus(status, pageable);
    }
    
    // Find overdue borrows
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> findOverdueBorrows() {
        return borrowedRepository.findOverdueRecords(LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> findOverdueBorrows(Pageable pageable) {
        return borrowedRepository.findOverdueRecords(LocalDate.now(), pageable);
    }
    
    // Business logic methods
    
    @Override
    public Borrowed returnBook(Long borrowedId, LocalDate returnDate) {
        Borrowed borrowed = borrowedRepository.findById(borrowedId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", borrowedId));
        
        // Validate not already returned
        if (borrowed.getStatus() == BorrowStatus.RETURNED || borrowed.getStatus() == BorrowStatus.OVERDUE) {
            throw new IllegalStateException("Book has already been returned");
        }
        
        if (returnDate.isBefore(borrowed.getBorrowDate())) {
            throw new IllegalArgumentException("Return date cannot be before borrow date");
        }
        
        // Check if return is overdue
        boolean isOverdue = returnDate.isAfter(borrowed.getDueDate());
        
        if (isOverdue) {
            long overdueDays = ChronoUnit.DAYS.between(borrowed.getDueDate(), returnDate);
            BigDecimal fineAmount = DAILY_FINE_RATE.multiply(BigDecimal.valueOf(overdueDays));
            
            borrowed.setStatus(BorrowStatus.OVERDUE);
            
            Fine fine = new Fine();
            fine.setBorrowed(borrowed);
            fine.setAmount(fineAmount);
            fine.setAssessedDate(returnDate);
            fine.setStatus(FineStatus.PENDING);
            fine.setReason("Overdue return: " + overdueDays + " day(s) late at $" + DAILY_FINE_RATE + " per day");
            
            fineRepository.save(fine);
        } else {
            borrowed.setStatus(BorrowStatus.RETURNED);
        }
        
        // Update borrowed record with return date
        borrowed.setReturnDate(returnDate);
        
        // Update book copy status back to AVAILABLE
        BookCopy bookCopy = borrowed.getBookCopy();
        if (bookCopy != null) {
            bookCopy.setStatus(BookCopyStatus.AVAILABLE);
            bookCopyRepository.save(bookCopy);
        }
        
        return borrowedRepository.save(borrowed);
    }
}
