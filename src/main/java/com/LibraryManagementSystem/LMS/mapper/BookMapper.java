package com.LibraryManagementSystem.LMS.mapper;

import com.LibraryManagementSystem.LMS.dto.AuthorResponseDTO;
import com.LibraryManagementSystem.LMS.dto.BookRequestDTO;
import com.LibraryManagementSystem.LMS.dto.BookResponseDTO;
import com.LibraryManagementSystem.LMS.dto.GenreResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Author;
import com.LibraryManagementSystem.LMS.entity.Book;
import com.LibraryManagementSystem.LMS.entity.Genre;
import com.LibraryManagementSystem.LMS.entity.Publisher;
import com.LibraryManagementSystem.LMS.repository.AuthorRepository;
import com.LibraryManagementSystem.LMS.repository.GenreRepository;
import com.LibraryManagementSystem.LMS.repository.PublisherRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {
    
    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;
    private final PublisherMapper publisherMapper;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    
    public BookMapper(AuthorMapper authorMapper, GenreMapper genreMapper, PublisherMapper publisherMapper,
                      AuthorRepository authorRepository, GenreRepository genreRepository, 
                      PublisherRepository publisherRepository) {
        this.authorMapper = authorMapper;
        this.genreMapper = genreMapper;
        this.publisherMapper = publisherMapper;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
    }
    
    public BookResponseDTO toResponseDTO(Book book) {
        if (book == null) {
            return null;
        }
        
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setIsbn(book.getIsbn());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setLanguage(book.getLanguage());
        dto.setPageCount(book.getPageCount());
        dto.setStatus(book.getStatus());
        
        // Map publisher
        if (book.getPublisher() != null) {
            dto.setPublisher(publisherMapper.toResponseDTO(book.getPublisher()));
        }
        
        // Map authors
        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            Set<AuthorResponseDTO> authorDTOs = book.getAuthors().stream()
                    .map(authorMapper::toResponseDTO)
                    .collect(Collectors.toSet());
            dto.setAuthors(authorDTOs);
        }
        
        // Map genres
        if (book.getGenres() != null && !book.getGenres().isEmpty()) {
            Set<GenreResponseDTO> genreDTOs = book.getGenres().stream()
                    .map(genreMapper::toResponseDTO)
                    .collect(Collectors.toSet());
            dto.setGenres(genreDTOs);
        }
        
        return dto;
    }
    
    public Book toEntity(BookRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        Book book = new Book();
        book.setIsbn(requestDTO.getIsbn());
        book.setTitle(requestDTO.getTitle());
        book.setDescription(requestDTO.getDescription());
        book.setPublicationDate(requestDTO.getPublicationDate());
        book.setLanguage(requestDTO.getLanguage());
        book.setPageCount(requestDTO.getPageCount());
        
        // Fetch and set publisher
        if (requestDTO.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(requestDTO.getPublisherId())
                    .orElse(null);
            book.setPublisher(publisher);
        }
        
        // Fetch and set authors
        if (requestDTO.getAuthorIds() != null && !requestDTO.getAuthorIds().isEmpty()) {
            Set<Author> authors = new HashSet<>();
            for (Long authorId : requestDTO.getAuthorIds()) {
                authorRepository.findById(authorId).ifPresent(authors::add);
            }
            book.setAuthors(authors);
        }
        
        // Fetch and set genres
        if (requestDTO.getGenreIds() != null && !requestDTO.getGenreIds().isEmpty()) {
            Set<Genre> genres = new HashSet<>();
            for (Long genreId : requestDTO.getGenreIds()) {
                genreRepository.findById(genreId).ifPresent(genres::add);
            }
            book.setGenres(genres);
        }
        
        return book;
    }
    
    public void updateEntityFromRequest(Book book, BookRequestDTO requestDTO) {
        if (book == null || requestDTO == null) {
            return;
        }
        
        if (requestDTO.getIsbn() != null) {
            book.setIsbn(requestDTO.getIsbn());
        }
        if (requestDTO.getTitle() != null) {
            book.setTitle(requestDTO.getTitle());
        }
        if (requestDTO.getDescription() != null) {
            book.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getPublicationDate() != null) {
            book.setPublicationDate(requestDTO.getPublicationDate());
        }
        if (requestDTO.getLanguage() != null) {
            book.setLanguage(requestDTO.getLanguage());
        }
        if (requestDTO.getPageCount() != null) {
            book.setPageCount(requestDTO.getPageCount());
        }
        
        // Update publisher
        if (requestDTO.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(requestDTO.getPublisherId())
                    .orElse(null);
            book.setPublisher(publisher);
        }
        
        // Update authors
        if (requestDTO.getAuthorIds() != null) {
            Set<Author> authors = new HashSet<>();
            for (Long authorId : requestDTO.getAuthorIds()) {
                authorRepository.findById(authorId).ifPresent(authors::add);
            }
            book.setAuthors(authors);
        }
        
        // Update genres
        if (requestDTO.getGenreIds() != null) {
            Set<Genre> genres = new HashSet<>();
            for (Long genreId : requestDTO.getGenreIds()) {
                genreRepository.findById(genreId).ifPresent(genres::add);
            }
            book.setGenres(genres);
        }
    }
}
