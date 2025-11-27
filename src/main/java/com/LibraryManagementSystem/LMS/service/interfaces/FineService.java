package com.LibraryManagementSystem.LMS.service.interfaces;

import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.enums.FineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FineService {
    
    // Core CRUD methods
    Fine create(Fine fine);

    Fine getById(Long id);

    List<Fine> getAll();

    Page<Fine> getAllPaginated(Pageable pageable);

    Fine update(Long id, Fine fine);

    void delete(Long id);
    
    // Find fine by borrowed record
    Fine findByBorrowedId(Long borrowedId);
    
    // Find by Status
    List<Fine> findByStatus(FineStatus status);
    
    Page<Fine> findByStatus(FineStatus status, Pageable pageable);
    
    // Find by User
    List<Fine> findByUserId(Long userId);
    
    Page<Fine> findByUserId(Long userId, Pageable pageable);
    
    // Find pending fines by user
    List<Fine> findPendingFinesByUserId(Long userId);
    
    Page<Fine> findPendingFinesByUserId(Long userId, Pageable pageable);
    
    // Find by date range
    List<Fine> findByAssessedDateBetween(LocalDate startDate, LocalDate endDate);
    
    Page<Fine> findByAssessedDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // Find by amount
    List<Fine> findByAmountGreaterThan(BigDecimal amount);
    
    Page<Fine> findByAmountGreaterThan(BigDecimal amount, Pageable pageable);
    
    // Business logic methods
    Fine payFine(Long fineId);
    
    Fine waiveFine(Long fineId);
    
    Fine assessFineForOverdue(Long borrowedId, BigDecimal dailyRate);
    
    // Calculation methods
    BigDecimal calculateTotalPendingFinesByUserId(Long userId);
    
    BigDecimal calculateTotalFinesByStatus(FineStatus status);
    
    long countByStatus(FineStatus status);
    
    boolean existsByBorrowedId(Long borrowedId);
    
    boolean hasUserPendingFines(Long userId);
}

