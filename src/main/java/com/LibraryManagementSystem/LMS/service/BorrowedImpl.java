package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.enums.BorrowStatus;
import com.LibraryManagementSystem.LMS.enums.BookCopyStatus;
import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;
import com.LibraryManagementSystem.LMS.repository.BorrowedRepository;
import com.LibraryManagementSystem.LMS.repository.BookCopyRepository;
import com.LibraryManagementSystem.LMS.repository.UserRepository;
import com.LibraryManagementSystem.LMS.service.interfaces.BorrowedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BorrowedImpl implements BorrowedService {
    
    private final BorrowedRepository borrowedRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    
    public BorrowedImpl(BorrowedRepository borrowedRepository, 
                        BookCopyRepository bookCopyRepository,
                        UserRepository userRepository) {
        this.borrowedRepository = borrowedRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
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
    
    // Find by BookCopy
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> findByBookCopyId(Long bookCopyId) {
        return borrowedRepository.findByBookCopyId(bookCopyId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> findByBookCopyId(Long bookCopyId, Pageable pageable) {
        return borrowedRepository.findByBookCopyId(bookCopyId, pageable);
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
    
    // Find by User and Status
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> findByUserIdAndStatus(Long userId, BorrowStatus status) {
        return borrowedRepository.findByUserIdAndStatus(userId, status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> findByUserIdAndStatus(Long userId, BorrowStatus status, Pageable pageable) {
        return borrowedRepository.findByUserIdAndStatus(userId, status, pageable);
    }
    
    // Find active borrows
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> findActiveBorrowsByUserId(Long userId) {
        return borrowedRepository.findActiveBorrowsByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> findActiveBorrowsByUserId(Long userId, Pageable pageable) {
        return borrowedRepository.findActiveBorrowsByUserId(userId, pageable);
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
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> findOverdueBorrowsByUserId(Long userId) {
        return borrowedRepository.findOverdueRecordsByUserId(userId, LocalDate.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> findOverdueBorrowsByUserId(Long userId, Pageable pageable) {
        return borrowedRepository.findOverdueRecordsByUserId(userId, LocalDate.now(), pageable);
    }
    
    // Find by date ranges
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> findByBorrowDateBetween(LocalDate startDate, LocalDate endDate) {
        return borrowedRepository.findByBorrowDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> findByBorrowDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return borrowedRepository.findByBorrowDateBetween(startDate, endDate, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> findByDueDateBetween(LocalDate startDate, LocalDate endDate) {
        return borrowedRepository.findRecordsDueSoon(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> findByDueDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return borrowedRepository.findRecordsDueSoon(startDate, endDate, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Borrowed> findByReturnDateBetween(LocalDate startDate, LocalDate endDate) {
        // Using native query approach since specific method not in repository
        return borrowedRepository.findAll().stream()
                .filter(b -> b.getReturnDate() != null && 
                            !b.getReturnDate().isBefore(startDate) && 
                            !b.getReturnDate().isAfter(endDate))
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Borrowed> findByReturnDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // For paginated version, use repository findAll and filter
        return borrowedRepository.findAll(pageable)
                .map(b -> b.getReturnDate() != null && 
                         !b.getReturnDate().isBefore(startDate) && 
                         !b.getReturnDate().isAfter(endDate) ? b : null)
                .map(b -> b);
    }
    
    // Business logic methods
    
    @Override
    public Borrowed returnBook(Long borrowedId, LocalDate returnDate) {
        Borrowed borrowed = borrowedRepository.findById(borrowedId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", borrowedId));
        
        // Validate not already returned
        if (borrowed.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalStateException("Book has already been returned");
        }
        
        // Update borrowed record
        borrowed.setReturnDate(returnDate);
        borrowed.setStatus(BorrowStatus.RETURNED);
        
        // Update book copy status back to AVAILABLE
        BookCopy bookCopy = borrowed.getBookCopy();
        if (bookCopy != null) {
            bookCopy.setStatus(BookCopyStatus.AVAILABLE);
            bookCopyRepository.save(bookCopy);
        }
        
        return borrowedRepository.save(borrowed);
    }
    
    @Override
    public void markOverdue() {
        LocalDate currentDate = LocalDate.now();
        List<Borrowed> overdueRecords = borrowedRepository.findOverdueRecords(currentDate);
        
        for (Borrowed borrowed : overdueRecords) {
            borrowed.setStatus(BorrowStatus.OVERDUE);
        }
        
        borrowedRepository.saveAll(overdueRecords);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isBookCopyAvailable(Long bookCopyId) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new ResourceNotFoundException("BookCopy", "id", bookCopyId));
        
        return bookCopy.getStatus() == BookCopyStatus.AVAILABLE;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasUserExceededBorrowLimit(Long userId, int limit) {
        long activeCount = borrowedRepository.countActiveBorrowsByUserId(userId);
        return activeCount >= limit;
    }
    
    @Override
    @Transactional(readOnly = true)
    public int countActiveBorrowsByUserId(Long userId) {
        return (int) borrowedRepository.countActiveBorrowsByUserId(userId);
    }
}
