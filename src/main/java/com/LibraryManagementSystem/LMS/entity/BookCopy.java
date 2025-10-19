package com.LibraryManagementSystem.LMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.LibraryManagementSystem.LMS.enums.BookCopyStatus;


@Entity
@Table(name = "book_copies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCopy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String barcode;
    
    @Column(length = 20, nullable = false)
    private String condition = "NEW"; // NEW, GOOD, FAIR, POOR
    
    @Column(length = 20, nullable = false)
    private BookCopyStatus status = BookCopyStatus.AVAILABLE; 
    
    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;
    
    @Column(length = 100)
    private String location;
    
    // MANY-TO-ONE: Many copies belong to one book
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @PrePersist
    @PreUpdate
    private void beforeSave() {
        if(this.status == null) {
            this.status = BookCopyStatus.AVAILABLE;
        }
    }   
}
