package com.LibraryManagementSystem.LMS.dto;

import com.LibraryManagementSystem.LMS.enums.FineStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FineRequestDTO {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Assessed date is required")
    private LocalDate assessedDate;

    private FineStatus status; // Optional - defaults to PENDING if not provided

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    @NotNull(message = "Borrowed ID is required")
    private Long borrowedId;

    public FineRequestDTO() {
    }

    public FineRequestDTO(BigDecimal amount, LocalDate assessedDate, FineStatus status, 
                          String reason, Long borrowedId) {
        this.amount = amount;
        this.assessedDate = assessedDate;
        this.status = status;
        this.reason = reason;
        this.borrowedId = borrowedId;
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

    public Long getBorrowedId() {
        return borrowedId;
    }

    public void setBorrowedId(Long borrowedId) {
        this.borrowedId = borrowedId;
    }
}
