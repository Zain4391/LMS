package com.LibraryManagementSystem.LMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.LibraryManagementSystem.LMS.enums.BorrowStatus;


@Entity
@Table(name = "borrowed")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Borrowed {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;
    
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    
    @Column(name = "return_date") // Null means not returned yet
    private LocalDate returnDate; 
    
    @Column(length = 20, nullable = false)
    private BorrowStatus status = BorrowStatus.BORROWED; 
    
    // MANY-TO-ONE: Many borrowed records can belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // MANY-TO-ONE: Many borrowed records can be for one book copy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_copy_id", nullable = false)
    private BookCopy bookCopy;
    
    @PrePersist
    private void setDefaultDueDate() {
        if (this.borrowDate != null && this.dueDate == null) {
            this.dueDate = this.borrowDate.plusDays(14);
        }
    }
}