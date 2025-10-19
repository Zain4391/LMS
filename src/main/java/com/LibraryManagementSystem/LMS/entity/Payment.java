package com.LibraryManagementSystem.LMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.LibraryManagementSystem.LMS.enums.PaymentStatus;
import com.LibraryManagementSystem.LMS.enums.PaymentMethod;


@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
    
    @Column(name = "payment_method", length = 20, nullable = false)
    private PaymentMethod paymentMethod;
    
    @Column(name = "transaction_id", length = 100)
    private String transactionId;
    
    @Column(length = 20, nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;
    
    // MANY-TO-ONE: Many payments can be for one fine (supports partial payments)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fine_id", nullable = false)
    private Fine fine;
}