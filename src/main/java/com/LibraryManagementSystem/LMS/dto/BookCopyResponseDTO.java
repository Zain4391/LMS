package com.LibraryManagementSystem.LMS.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.LibraryManagementSystem.LMS.enums.BookCopyStatus;
import java.time.LocalDate;

public class BookCopyResponseDTO {

    private Long id;
    private String barcode;
    private String condition;
    private BookCopyStatus status;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate acquisitionDate;
    
    private String location;
    private BookResponseDTO book;

    public BookCopyResponseDTO() {
    }

    public BookCopyResponseDTO(Long id, String barcode, String condition, BookCopyStatus status,
                               LocalDate acquisitionDate, String location, BookResponseDTO book) {
        this.id = id;
        this.barcode = barcode;
        this.condition = condition;
        this.status = status;
        this.acquisitionDate = acquisitionDate;
        this.location = location;
        this.book = book;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BookCopyStatus getStatus() {
        return status;
    }

    public void setStatus(BookCopyStatus status) {
        this.status = status;
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

    public BookResponseDTO getBook() {
        return book;
    }

    public void setBook(BookResponseDTO book) {
        this.book = book;
    }
}
