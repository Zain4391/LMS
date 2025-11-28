package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.entity.Payment;
import com.LibraryManagementSystem.LMS.enums.FineStatus;
import com.LibraryManagementSystem.LMS.enums.PaymentStatus;
import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;
import com.LibraryManagementSystem.LMS.repository.BorrowedRepository;
import com.LibraryManagementSystem.LMS.repository.FineRepository;
import com.LibraryManagementSystem.LMS.repository.PaymentRepository;
import com.LibraryManagementSystem.LMS.service.interfaces.FineService;
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
public class FineServiceImpl implements FineService {
    
    private final FineRepository fineRepository;
    private final BorrowedRepository borrowedRepository;
    private final PaymentRepository paymentRepository;

    public FineServiceImpl(FineRepository fineRepository, BorrowedRepository borrowedRepository,
                          PaymentRepository paymentRepository) {
        this.fineRepository = fineRepository;
        this.borrowedRepository = borrowedRepository;
        this.paymentRepository = paymentRepository;
    }
    
    // Core CRUD methods
    
    @Override
    public Fine create(Fine fine) {
        // Validate borrowed record exists
        if (fine.getBorrowed() != null && fine.getBorrowed().getId() != null) {
            Borrowed borrowed = borrowedRepository.findById(fine.getBorrowed().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", fine.getBorrowed().getId()));
            
            // Check if fine already exists for this borrowed record
            if (fineRepository.existsByBorrowed(borrowed)) {
                throw new IllegalStateException("Fine already exists for borrowed record with ID " + borrowed.getId());
            }
            
            fine.setBorrowed(borrowed);
        }
        
        return fineRepository.save(fine);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Fine getById(Long id) {
        return fineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Fine> getAll() {
        return fineRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Fine> getAllPaginated(Pageable pageable) {
        return fineRepository.findAll(pageable);
    }
    
    @Override
    public Fine update(Long id, Fine fine) {
        Fine existingFine = fineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine", "id", id));
        
        existingFine.setAmount(fine.getAmount());
        existingFine.setAssessedDate(fine.getAssessedDate());
        existingFine.setStatus(fine.getStatus());
        existingFine.setReason(fine.getReason());
        existingFine.setBorrowed(fine.getBorrowed());
        
        return fineRepository.save(existingFine);
    }
    
    @Override
    public void delete(Long id) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine", "id", id));
        fineRepository.delete(fine);
    }
    
    // Find fine by borrowed record
    
    @Override
    @Transactional(readOnly = true)
    public Fine findByBorrowedId(Long borrowedId) {
        Borrowed borrowed = borrowedRepository.findById(borrowedId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", borrowedId));
        
        return fineRepository.findByBorrowed(borrowed)
                .orElseThrow(() -> new ResourceNotFoundException("Fine for borrowed record", "borrowedId", borrowedId));
    }
    
    // Find by Status
    
    @Override
    @Transactional(readOnly = true)
    public List<Fine> findByStatus(FineStatus status) {
        return fineRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Fine> findByStatus(FineStatus status, Pageable pageable) {
        return fineRepository.findByStatus(status, pageable);
    }
    
    // Find by User
    
    @Override
    @Transactional(readOnly = true)
    public List<Fine> findByUserId(Long userId) {
        return fineRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Fine> findByUserId(Long userId, Pageable pageable) {
        return fineRepository.findByUserId(userId, pageable);
    }
    
    
    @Override
    public Fine payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine", "id", fineId));
        
        // Validate that fine is currently pending
        if (fine.getStatus() != FineStatus.PENDING) {
            throw new IllegalStateException("Only pending fines can be paid. Current status: " + fine.getStatus());
        }
        
        // Update fine status to PAID
        fine.setStatus(FineStatus.PAID);
        
        // Update all payments to COMPLETED
        List<Payment> payments = paymentRepository.findByFine(fine);
        for (Payment payment : payments) {
            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(payment);
        }
        
        return fineRepository.save(fine);
    }
    
    @Override
    public Fine assessFineForOverdue(Long borrowedId, BigDecimal dailyRate) {
        Borrowed borrowed = borrowedRepository.findById(borrowedId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", borrowedId));
        
        // Check if fine already exists
        if (fineRepository.existsByBorrowed(borrowed)) {
            throw new IllegalStateException("Fine already exists for this borrowed record");
        }
        
        // Calculate days overdue
        LocalDate dueDate = borrowed.getDueDate();
        LocalDate returnDate = borrowed.getReturnDate() != null ? borrowed.getReturnDate() : LocalDate.now();
        
        if (!returnDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("Book is not overdue. Due date: " + dueDate + ", Return date: " + returnDate);
        }
        
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        BigDecimal fineAmount = dailyRate.multiply(BigDecimal.valueOf(daysOverdue));
        
        // Create fine
        Fine fine = new Fine();
        fine.setAmount(fineAmount);
        fine.setAssessedDate(LocalDate.now());
        fine.setStatus(FineStatus.PENDING);
        fine.setReason("Book returned " + daysOverdue + " day(s) late at " + dailyRate + " per day");
        fine.setBorrowed(borrowed);
        
        return fineRepository.save(fine);
    }
}

