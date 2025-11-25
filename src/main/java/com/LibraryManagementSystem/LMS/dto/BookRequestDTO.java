package com.LibraryManagementSystem.LMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public class BookRequestDTO {

    @NotBlank(message = "ISBN is mandatory")
    @Size(max = 20, message = "ISBN must be at max 20 characters")
    private String isbn;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 300, message = "Title must be at max 300 characters")
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    private LocalDate publicationDate;

    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language;

    @Positive(message = "Page count must be a positive number")
    private Integer pageCount;

    private Long publisherId;

    @NotEmpty(message = "At least one author is required")
    private Set<Long> authorIds;

    private Set<Long> genreIds;

    public BookRequestDTO() {
    }

    public BookRequestDTO(String isbn, String title, String description, LocalDate publicationDate, 
                          String language, Integer pageCount, Long publisherId, 
                          Set<Long> authorIds, Set<Long> genreIds) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.language = language;
        this.pageCount = pageCount;
        this.publisherId = publisherId;
        this.authorIds = authorIds;
        this.genreIds = genreIds;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Set<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(Set<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public Set<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(Set<Long> genreIds) {
        this.genreIds = genreIds;
    }
}
