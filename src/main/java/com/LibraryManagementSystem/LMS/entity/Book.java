package com.LibraryManagementSystem.LMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.LibraryManagementSystem.LMS.enums.BookStatus;


@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;
    
    @Column(nullable = false, length = 300)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Column(name = "publication_date")
    private LocalDate publicationDate;
    
    @Column(length = 50)
    private String language;
    
    @Column(name = "page_count")
    private Integer pageCount;
    
    @Column(length = 20, nullable = false)
    private BookStatus status = BookStatus.AVAILABLE;
    
    // MANY-TO-ONE: Many books can have one publisher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
    
    // MANY-TO-MANY: A book can have multiple authors
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();
    
    // MANY-TO-MANY: A book can have multiple genres
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_genres",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void beforeSave() {
        if(this.status == null) {
            this.status = BookStatus.AVAILABLE;
        }
    }
}