package com.LibraryManagementSystem.LMS.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.LibraryManagementSystem.LMS.enums.BorrowStatus;
import java.time.LocalDate;

public class BorrowedResponseDTO {

    private Long id;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate borrowDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    
    private BorrowStatus status;
    
    // Nested DTOs for related entities
    private UserSummaryDTO user;
    private BookCopyResponseDTO bookCopy;

    public BorrowedResponseDTO() {
    }

    public BorrowedResponseDTO(Long id, LocalDate borrowDate, LocalDate dueDate, 
                               LocalDate returnDate, BorrowStatus status,
                               UserSummaryDTO user, BookCopyResponseDTO bookCopy) {
        this.id = id;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.user = user;
        this.bookCopy = bookCopy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }

    public UserSummaryDTO getUser() {
        return user;
    }

    public void setUser(UserSummaryDTO user) {
        this.user = user;
    }

    public BookCopyResponseDTO getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopyResponseDTO bookCopy) {
        this.bookCopy = bookCopy;
    }
}
