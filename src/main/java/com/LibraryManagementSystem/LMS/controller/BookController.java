package com.LibraryManagementSystem.LMS.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LibraryManagementSystem.LMS.dto.BookRequestDTO;
import com.LibraryManagementSystem.LMS.dto.BookResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Book;
import com.LibraryManagementSystem.LMS.enums.BookStatus;
import com.LibraryManagementSystem.LMS.mapper.BookMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.BookService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    // Create new Book
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookRequestDTO bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        Book createdBook = bookService.create(book);
        BookResponseDTO responseDTO = bookMapper.toResponseDTO(createdBook); 
        
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get book by id
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.getById(id);
        BookResponseDTO responseDTO = bookMapper.toResponseDTO(book);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all books (with optional pagination)
    @GetMapping
    public ResponseEntity<?> getAllBooks(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            // Return paginated response
            Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Book> bookPage = bookService.getAllPaginated(pageable);
            Page<BookResponseDTO> responsePage = bookPage.map(bookMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            // Return list response
            List<Book> books = bookService.getAll();
            List<BookResponseDTO> responseDTOs = books.stream()
                    .map(bookMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Update book by ID
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO requestDTO) {
        Book book = bookMapper.toEntity(requestDTO);
        Book updatedBook = bookService.update(id, book);
        BookResponseDTO responseDTO = bookMapper.toResponseDTO(updatedBook);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete book by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Search books by ISBN
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponseDTO> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookService.findByIsbn(isbn)
                .orElseThrow(() -> new com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException("Book", "isbn", isbn));
        BookResponseDTO responseDTO = bookMapper.toResponseDTO(book);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Search books by title
    @GetMapping("/search/title")
    public ResponseEntity<?> searchBooksByTitle(
            @RequestParam String title,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Book> bookPage = bookService.findByTitleContaining(title, pageable);
            Page<BookResponseDTO> responsePage = bookPage.map(bookMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Book> books = bookService.findByTitleContaining(title);
            List<BookResponseDTO> responseDTOs = books.stream()
                    .map(bookMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Filter books by status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBooksByStatus(
            @PathVariable BookStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Book> bookPage = bookService.findByStatus(status, pageable);
            Page<BookResponseDTO> responsePage = bookPage.map(bookMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Book> books = bookService.findByStatus(status);
            List<BookResponseDTO> responseDTOs = books.stream()
                    .map(bookMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Filter books by publisher ID
    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<?> getBooksByPublisher(
            @PathVariable Long publisherId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Book> bookPage = bookService.findByPublisherId(publisherId, pageable);
            Page<BookResponseDTO> responsePage = bookPage.map(bookMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Book> books = bookService.findByPublisherId(publisherId);
            List<BookResponseDTO> responseDTOs = books.stream()
                    .map(bookMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Filter books by language
    @GetMapping("/language/{language}")
    public ResponseEntity<?> getBooksByLanguage(
            @PathVariable String language,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Book> bookPage = bookService.findByLanguage(language, pageable);
            Page<BookResponseDTO> responsePage = bookPage.map(bookMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Book> books = bookService.findByLanguage(language);
            List<BookResponseDTO> responseDTOs = books.stream()
                    .map(bookMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Search books by author name
    @GetMapping("/search/author")
    public ResponseEntity<?> searchBooksByAuthor(
            @RequestParam String authorName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Book> bookPage = bookService.findByAuthorName(authorName, pageable);
            Page<BookResponseDTO> responsePage = bookPage.map(bookMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Book> books = bookService.findByAuthorName(authorName);
            List<BookResponseDTO> responseDTOs = books.stream()
                    .map(bookMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Search books by genre name
    @GetMapping("/search/genre")
    public ResponseEntity<?> searchBooksByGenre(
            @RequestParam String genreName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Book> bookPage = bookService.findByGenreName(genreName, pageable);
            Page<BookResponseDTO> responsePage = bookPage.map(bookMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Book> books = bookService.findByGenreName(genreName);
            List<BookResponseDTO> responseDTOs = books.stream()
                    .map(bookMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // General search (by title or author)
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Book> bookPage = bookService.searchBooks(keyword, pageable);
            Page<BookResponseDTO> responsePage = bookPage.map(bookMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Book> books = bookService.searchBooks(keyword);
            List<BookResponseDTO> responseDTOs = books.stream()
                    .map(bookMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Check if ISBN exists
    @GetMapping("/exists/isbn/{isbn}")
    public ResponseEntity<Boolean> checkIsbnExists(@PathVariable String isbn) {
        boolean exists = bookService.existsByIsbn(isbn);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Count books by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countBooksByStatus(@PathVariable BookStatus status) {
        long count = bookService.countByStatus(status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
