package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    Optional<Fine> findByBorrowed(Borrowed borrowed);
    
    List<Fine> findByStatus(FineStatus status);
    Page<Fine> findByStatus(FineStatus status, Pageable pageable);
    
    @Query("SELECT f FROM Fine f WHERE f.borrowed.user.id = :userId")
    List<Fine> findByUserId(@Param("userId") Long userId);

    @Query("SELECT f FROM Fine f WHERE f.borrowed.user.id = :userId")
    Page<Fine> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT f FROM Fine f WHERE f.borrowed.user.id = :userId AND f.status = com.LibraryManagementSystem.LMS.enums.FineStatus.PENDING")
    List<Fine> findPendingFinesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT f FROM Fine f WHERE f.borrowed.user.id = :userId AND f.status = com.LibraryManagementSystem.LMS.enums.FineStatus.PENDING")
    Page<Fine> findPendingFinesByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT f FROM Fine f WHERE f.assessedDate BETWEEN :startDate AND :endDate")
    List<Fine> findByAssessedDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT f FROM Fine f WHERE f.assessedDate BETWEEN :startDate AND :endDate")
    Page<Fine> findByAssessedDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    @Query("SELECT f FROM Fine f WHERE f.amount > :amount")
    List<Fine> findByAmountGreaterThan(@Param("amount") BigDecimal amount);
    
    @Query("SELECT f FROM Fine f WHERE f.amount > :amount")
    Page<Fine> findByAmountGreaterThan(@Param("amount") BigDecimal amount, Pageable pageable);
    
    @Query("SELECT SUM(f.amount) FROM Fine f WHERE f.borrowed.user.id = :userId AND f.status = com.LibraryManagementSystem.LMS.enums.FineStatus.PENDING")
    BigDecimal calculateTotalPendingFinesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(f.amount) FROM Fine f WHERE f.status = :status")
    BigDecimal calculateTotalFinesByStatus(@Param("status") FineStatus status);
    
    long countByStatus(FineStatus status);
    
    boolean existsByBorrowed(Borrowed borrowed);
}
