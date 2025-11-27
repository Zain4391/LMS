package com.LibraryManagementSystem.LMS.service;

import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.entity.Payment;
import com.LibraryManagementSystem.LMS.enums.PaymentMethod;
import com.LibraryManagementSystem.LMS.enums.PaymentStatus;
import com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException;
import com.LibraryManagementSystem.LMS.repository.FineRepository;
import com.LibraryManagementSystem.LMS.repository.PaymentRepository;
import com.LibraryManagementSystem.LMS.service.interfaces.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final FineRepository fineRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, FineRepository fineRepository) {
        this.paymentRepository = paymentRepository;
        this.fineRepository = fineRepository;
    }

    // Core CRUD methods
    
    @Override
    public Payment create(Payment payment) {
        // Validate fine exists
        if (payment.getFine() != null && payment.getFine().getId() != null) {
            Fine fine = fineRepository.findById(payment.getFine().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Fine", "id", payment.getFine().getId()));
            payment.setFine(fine);
        }
        
        // Validate transaction ID uniqueness if provided
        if (payment.getTransactionId() != null && !payment.getTransactionId().isEmpty()) {
            if (paymentRepository.existsByTransactionId(payment.getTransactionId())) {
                throw new IllegalStateException("Payment with transaction ID " + payment.getTransactionId() + " already exists");
            }
        }
        
        return paymentRepository.save(payment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Payment getById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Payment> getAllPaginated(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }
    
    @Override
    public Payment update(Long id, Payment payment) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setPaymentDate(payment.getPaymentDate());
        existingPayment.setPaymentMethod(payment.getPaymentMethod());
        existingPayment.setTransactionId(payment.getTransactionId());
        existingPayment.setStatus(payment.getStatus());
        existingPayment.setFine(payment.getFine());
        
        return paymentRepository.save(existingPayment);
    }
    
    @Override
    public void delete(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        paymentRepository.delete(payment);
    }
    
    // Find payment by transaction ID
    
    @Override
    @Transactional(readOnly = true)
    public Payment findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "transactionId", transactionId));
    }
    
    // Find payments by fine
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByFineId(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine", "id", fineId));
        return paymentRepository.findByFine(fine);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Payment> findByFineId(Long fineId, Pageable pageable) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine", "id", fineId));
        return paymentRepository.findByFine(fine, pageable);
    }
    
    // Find by Status
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Payment> findByStatus(PaymentStatus status, Pageable pageable) {
        return paymentRepository.findByStatus(status, pageable);
    }
    
    // Find by Payment Method
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByPaymentMethod(PaymentMethod paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Payment> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable) {
        return paymentRepository.findByPaymentMethod(paymentMethod, pageable);
    }
    
    // Find by User
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Payment> findByUserId(Long userId, Pageable pageable) {
        return paymentRepository.findByUserId(userId, pageable);
    }
    
    // Find by date range
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate, pageable);
    }
    
    // Find completed payments by method
    
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findCompletedPaymentsByMethod(PaymentMethod method) {
        return paymentRepository.findCompletedPaymentsByMethod(method);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Payment> findCompletedPaymentsByMethod(PaymentMethod method, Pageable pageable) {
        return paymentRepository.findCompletedPaymentsByMethod(method, pageable);
    }
    
    // Business logic methods
    
    @Override
    public Payment processPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        
        // Validate that payment is currently pending
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be processed. Current status: " + payment.getStatus());
        }
        
        // Set payment date if not already set
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }
        
        return paymentRepository.save(payment);
    }
    
    @Override
    public Payment completePayment(Long paymentId, String transactionId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        
        // Validate that payment is not already completed
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Payment is already completed");
        }
        
        // Validate transaction ID uniqueness if different from current
        if (transactionId != null && !transactionId.equals(payment.getTransactionId())) {
            if (paymentRepository.existsByTransactionId(transactionId)) {
                throw new IllegalStateException("Payment with transaction ID " + transactionId + " already exists");
            }
        }
        
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId(transactionId);
        payment.setPaymentDate(LocalDate.now());
        
        return paymentRepository.save(payment);
    }
    
    @Override
    public Payment failPayment(Long paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        
        // Validate that payment is not already completed
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot fail a completed payment");
        }
        
        payment.setStatus(PaymentStatus.FAILED);
        // Store failure reason in transaction ID field if needed, or handle separately
        
        return paymentRepository.save(payment);
    }
    
    @Override
    public Payment refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        
        // Validate that payment is currently completed
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Only completed payments can be refunded. Current status: " + payment.getStatus());
        }
        
        // Create a new refund payment record with negative amount
        Payment refund = new Payment();
        refund.setAmount(payment.getAmount().negate());
        refund.setPaymentDate(LocalDate.now());
        refund.setPaymentMethod(payment.getPaymentMethod());
        refund.setTransactionId("REFUND-" + payment.getId() + "-" + System.currentTimeMillis());
        refund.setStatus(PaymentStatus.COMPLETED);
        refund.setFine(payment.getFine());
        
        return paymentRepository.save(refund);
    }
    
    // Calculation methods
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPaidAmountByFineId(Long fineId) {
        BigDecimal total = paymentRepository.calculateTotalPaidAmountByFineId(fineId);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPaidAmountByUserId(Long userId) {
        BigDecimal total = paymentRepository.calculateTotalPaidAmountByUserId(userId);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalRevenueBetween(LocalDate startDate, LocalDate endDate) {
        BigDecimal total = paymentRepository.calculateTotalRevenueBetween(startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByTransactionId(String transactionId) {
        return paymentRepository.existsByTransactionId(transactionId);
    }
}
