package com.LibraryManagementSystem.LMS.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.LibraryManagementSystem.LMS.enums.BookStatus;
import java.time.LocalDate;
import java.util.Set;

public class BookResponseDTO {

    private Long id;
    private String isbn;
    private String title;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    private String language;
    private Integer pageCount;
    private BookStatus status;
    private PublisherResponseDTO publisher;
    private Set<AuthorResponseDTO> authors;
    private Set<GenreResponseDTO> genres;

    public BookResponseDTO() {
    }

    public BookResponseDTO(Long id, String isbn, String title, String description, LocalDate publicationDate,
                           String language, Integer pageCount, BookStatus status, PublisherResponseDTO publisher,
                           Set<AuthorResponseDTO> authors, Set<GenreResponseDTO> genres) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.language = language;
        this.pageCount = pageCount;
        this.status = status;
        this.publisher = publisher;
        this.authors = authors;
        this.genres = genres;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public BookStatus getStatus() {
        return status;
    }
    public void setStatus(BookStatus status) {
        this.status = status;
    }
    public PublisherResponseDTO getPublisher() {
        return publisher;
    }
    public void setPublisher(PublisherResponseDTO publisher) {
        this.publisher = publisher;
    }
    public Set<AuthorResponseDTO> getAuthors() {
        return authors;
    }
    public void setAuthors(Set<AuthorResponseDTO> authors) {
        this.authors = authors;
    }
    public Set<GenreResponseDTO> getGenres() {
        return genres;
    }
    public void setGenres(Set<GenreResponseDTO> genres) {
        this.genres = genres;
    }
}
