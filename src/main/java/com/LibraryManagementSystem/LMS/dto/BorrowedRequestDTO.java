package com.LibraryManagementSystem.LMS.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BorrowedRequestDTO {

    @NotNull(message = "Borrow date is required")
    private LocalDate borrowDate;

    private LocalDate dueDate; // Optional - will be auto-calculated if not provided (borrowDate + 14 days)

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Book copy ID is required")
    private Long bookCopyId;

    public BorrowedRequestDTO() {
    }

    public BorrowedRequestDTO(LocalDate borrowDate, LocalDate dueDate, Long userId, Long bookCopyId) {
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.userId = userId;
        this.bookCopyId = bookCopyId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookCopyId() {
        return bookCopyId;
    }

    public void setBookCopyId(Long bookCopyId) {
        this.bookCopyId = bookCopyId;
    }
}
