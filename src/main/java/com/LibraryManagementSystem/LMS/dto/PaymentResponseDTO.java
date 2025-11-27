package com.LibraryManagementSystem.LMS.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.LibraryManagementSystem.LMS.enums.PaymentMethod;
import com.LibraryManagementSystem.LMS.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public class PaymentResponseDTO {
    
    private Long id;
    
    private BigDecimal amount;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;
    
    private PaymentMethod paymentMethod;
    
    private String transactionId;
    
    private PaymentStatus status;
    
    // Nested DTO for related entity
    private FineResponseDTO fine;

    public PaymentResponseDTO() {
    }

    public PaymentResponseDTO(Long id, BigDecimal amount, LocalDate paymentDate, 
                             PaymentMethod paymentMethod, String transactionId, 
                             PaymentStatus status, FineResponseDTO fine) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status = status;
        this.fine = fine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public FineResponseDTO getFine() {
        return fine;
    }

    public void setFine(FineResponseDTO fine) {
        this.fine = fine;
    }
}

