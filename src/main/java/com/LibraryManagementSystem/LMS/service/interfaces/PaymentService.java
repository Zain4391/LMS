package com.LibraryManagementSystem.LMS.service.interfaces;

import com.LibraryManagementSystem.LMS.entity.Payment;
import com.LibraryManagementSystem.LMS.enums.PaymentMethod;
import com.LibraryManagementSystem.LMS.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentService {
    
    // Core CRUD methods
    Payment create(Payment payment);

    Payment getById(Long id);

    List<Payment> getAll();

    Page<Payment> getAllPaginated(Pageable pageable);

    Payment update(Long id, Payment payment);

    void delete(Long id);
    
    // Find payment by transaction ID
    Payment findByTransactionId(String transactionId);
    
    // Find payments by fine
    List<Payment> findByFineId(Long fineId);
    
    Page<Payment> findByFineId(Long fineId, Pageable pageable);
    
    // Find by Status
    List<Payment> findByStatus(PaymentStatus status);
    
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
    
    // Find by Payment Method
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
    
    Page<Payment> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable);
    
    // Find by User
    List<Payment> findByUserId(Long userId);
    
    Page<Payment> findByUserId(Long userId, Pageable pageable);
    
    // Find by date range
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    
    Page<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // Find completed payments by method
    List<Payment> findCompletedPaymentsByMethod(PaymentMethod method);
    
    Page<Payment> findCompletedPaymentsByMethod(PaymentMethod method, Pageable pageable);
    
    // Business logic methods
    Payment processPayment(Long paymentId);
    
    Payment completePayment(Long paymentId, String transactionId);
    
    Payment failPayment(Long paymentId, String reason);
    
    Payment refundPayment(Long paymentId);
    
    // Calculation methods
    BigDecimal calculateTotalPaidAmountByFineId(Long fineId);
    
    BigDecimal calculateTotalPaidAmountByUserId(Long userId);
    
    BigDecimal calculateTotalRevenueBetween(LocalDate startDate, LocalDate endDate);
    
    boolean existsByTransactionId(String transactionId);
}
