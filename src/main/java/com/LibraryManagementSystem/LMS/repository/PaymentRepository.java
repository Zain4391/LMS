package com.LibraryManagementSystem.LMS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.LibraryManagementSystem.LMS.entity.Payment;
import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.enums.PaymentStatus;
import com.LibraryManagementSystem.LMS.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByFine(Fine fine);
    Page<Payment> findByFine(Fine fine, Pageable pageable);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    List<Payment> findByStatus(PaymentStatus status);
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
    
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
    Page<Payment> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.fine.borrowed.user.id = :userId")
    List<Payment> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT p FROM Payment p WHERE p.fine.borrowed.user.id = :userId")
    Page<Payment> findByUserId(@Param("userId") Long userId, Pageable pageable);
    
    // Find payments by date range
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByPaymentDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    Page<Payment> findByPaymentDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    // Calculate total payments by fine
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.fine.id = :fineId AND p.status = com.LibraryManagementSystem.LMS.enums.PaymentStatus.COMPLETED")
    BigDecimal calculateTotalPaidAmountByFineId(@Param("fineId") Long fineId);
    
    // Calculate total payments by user
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.fine.borrowed.user.id = :userId AND p.status = com.LibraryManagementSystem.LMS.enums.PaymentStatus.COMPLETED")
    BigDecimal calculateTotalPaidAmountByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate AND p.status = com.LibraryManagementSystem.LMS.enums.PaymentStatus.COMPLETED")
    BigDecimal calculateTotalRevenueBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find successful payments by payment method
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = :method AND p.status = com.LibraryManagementSystem.LMS.enums.PaymentStatus.COMPLETED")
    List<Payment> findCompletedPaymentsByMethod(@Param("method") PaymentMethod method);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = :method AND p.status = com.LibraryManagementSystem.LMS.enums.PaymentStatus.COMPLETED")
    Page<Payment> findCompletedPaymentsByMethod(@Param("method") PaymentMethod method, Pageable pageable);
    
    boolean existsByTransactionId(String transactionId);
}
