package com.LibraryManagementSystem.LMS.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.LibraryManagementSystem.LMS.enums.FineStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public class FineResponseDTO {
    
    private Long id;
    
    private BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assessedDate;

    private FineStatus status;

    private String reason;

    // Nested DTO for related entity
    private BorrowedResponseDTO borrowed;

    public FineResponseDTO() {
    }

    public FineResponseDTO(Long id, BigDecimal amount, LocalDate assessedDate, 
                           FineStatus status, String reason, BorrowedResponseDTO borrowed) {
        this.id = id;
        this.amount = amount;
        this.assessedDate = assessedDate;
        this.status = status;
        this.reason = reason;
        this.borrowed = borrowed;
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

    public LocalDate getAssessedDate() {
        return assessedDate;
    }

    public void setAssessedDate(LocalDate assessedDate) {
        this.assessedDate = assessedDate;
    }

    public FineStatus getStatus() {
        return status;
    }

    public void setStatus(FineStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BorrowedResponseDTO getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(BorrowedResponseDTO borrowed) {
        this.borrowed = borrowed;
    }
}

