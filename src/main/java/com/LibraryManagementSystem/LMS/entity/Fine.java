package com.LibraryManagementSystem.LMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.LibraryManagementSystem.LMS.enums.FineStatus;

@Entity
@Table(name = "fines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "assessed_date", nullable = false)
    private LocalDate assessedDate;
    
    @Column(nullable = false)
    private FineStatus status = FineStatus.PENDING;
    
    @Column(length = 500)
    private String reason;
    
    // ONE-TO-ONE: One fine per borrowed record
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowed_id", nullable = false, unique = true)
    private Borrowed borrowed;
}