package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.enums.FineStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    
    // Find fine by borrowed record
    Optional<Fine> findByBorrowed(Borrowed borrowed);
    
    // Find fines by status
    List<Fine> findByStatus(FineStatus status);
    
    // Find fines by user (through borrowed record)
    @Query("SELECT f FROM Fine f WHERE f.borrowed.user.id = :userId")
    List<Fine> findByUserId(@Param("userId") Long userId);
    
    // Find pending fines by user
    @Query("SELECT f FROM Fine f WHERE f.borrowed.user.id = :userId AND f.status = 'PENDING'")
    List<Fine> findPendingFinesByUserId(@Param("userId") Long userId);
    
    // Find fines by date range
    @Query("SELECT f FROM Fine f WHERE f.assessedDate BETWEEN :startDate AND :endDate")
    List<Fine> findByAssessedDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find fines greater than amount
    @Query("SELECT f FROM Fine f WHERE f.amount > :amount")
    List<Fine> findByAmountGreaterThan(@Param("amount") BigDecimal amount);
    
    // Calculate total pending fines by user
    @Query("SELECT SUM(f.amount) FROM Fine f WHERE f.borrowed.user.id = :userId AND f.status = 'PENDING'")
    BigDecimal calculateTotalPendingFinesByUserId(@Param("userId") Long userId);
    
    // Calculate total fines by status
    @Query("SELECT SUM(f.amount) FROM Fine f WHERE f.status = :status")
    BigDecimal calculateTotalFinesByStatus(@Param("status") FineStatus status);
    
    // Count fines by status
    long countByStatus(FineStatus status);
    
    // Check if fine exists for borrowed record
    boolean existsByBorrowed(Borrowed borrowed);
}
