package com.LibraryManagementSystem.LMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class BookCopyRequestDTO {

    @NotBlank(message = "Barcode is mandatory")
    @Size(max = 50, message = "Barcode must not exceed 50 characters")
    private String barcode;

    @NotBlank(message = "Condition is mandatory")
    @Size(max = 20, message = "Condition must not exceed 20 characters")
    private String condition;

    private LocalDate acquisitionDate;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    public BookCopyRequestDTO() {
    }

    public BookCopyRequestDTO(String barcode, String condition, LocalDate acquisitionDate, 
                              String location, Long bookId) {
        this.barcode = barcode;
        this.condition = condition;
        this.acquisitionDate = acquisitionDate;
        this.location = location;
        this.bookId = bookId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
