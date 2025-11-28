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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Books", description = "Book management APIs - Manage book catalog including ISBN, titles, authors, genres, and availability status")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    // Create new Book
    @Operation(
            summary = "Create a new book",
            description = "Creates a new book with the provided details including title, ISBN, authors, genres, publisher, and other metadata"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "409", description = "Book with the same ISBN already exists")
    })
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(
            @Parameter(description = "Book details to create", required = true)
            @Valid @RequestBody BookRequestDTO bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        Book createdBook = bookService.create(book);
        BookResponseDTO responseDTO = bookMapper.toResponseDTO(createdBook); 
        
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get book by id
    @Operation(
            summary = "Get book by ID",
            description = "Retrieves a specific book's details by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found successfully",
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(
            @Parameter(description = "Book ID", required = true, example = "1")
            @PathVariable Long id) {
        Book book = bookService.getById(id);
        BookResponseDTO responseDTO = bookMapper.toResponseDTO(book);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all books (with optional pagination)
    @Operation(
            summary = "Get all books",
            description = "Retrieves all books in the system with optional pagination and sorting. If pagination parameters are not provided, returns a complete list."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<?> getAllBooks(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "title")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Update book",
            description = "Updates an existing book's information with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "Book not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "ISBN conflicts with existing book")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @Parameter(description = "Book ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated book details", required = true)
            @Valid @RequestBody BookRequestDTO requestDTO) {
        Book book = bookMapper.toEntity(requestDTO);
        Book updatedBook = bookService.update(id, book);
        BookResponseDTO responseDTO = bookMapper.toResponseDTO(updatedBook);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete book by ID
    @Operation(
            summary = "Delete book",
            description = "Permanently deletes a book from the system. Note: Cannot delete books that have active borrowed copies."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Cannot delete book - active borrows or copies exist")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book ID", required = true, example = "1")
            @PathVariable Long id) {
        bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Search books by ISBN
    @Operation(
            summary = "Get book by ISBN",
            description = "Retrieves a specific book by its unique ISBN number"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found successfully",
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book not found with the given ISBN")
    })
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponseDTO> getBookByIsbn(
            @Parameter(description = "Book ISBN", required = true, example = "978-0-13-468599-1")
            @PathVariable String isbn) {
        Book book = bookService.findByIsbn(isbn)
                .orElseThrow(() -> new com.LibraryManagementSystem.LMS.exception.ResourceNotFoundException("Book", "isbn", isbn));
        BookResponseDTO responseDTO = bookMapper.toResponseDTO(book);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Search books by title
    @Operation(
            summary = "Search books by title",
            description = "Searches for books containing the specified title text (case-insensitive) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping("/search/title")
    public ResponseEntity<?> searchBooksByTitle(
            @Parameter(description = "Title search keyword", required = true, example = "Java")
            @RequestParam String title,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Get books by status",
            description = "Filters books by their availability status (e.g., AVAILABLE, CHECKED_OUT, RESERVED) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBooksByStatus(
            @Parameter(description = "Book status", required = true, example = "AVAILABLE")
            @PathVariable BookStatus status,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Get books by publisher",
            description = "Retrieves all books published by a specific publisher with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Publisher not found with the given ID")
    })
    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<?> getBooksByPublisher(
            @Parameter(description = "Publisher ID", required = true, example = "1")
            @PathVariable Long publisherId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Get books by language",
            description = "Retrieves all books available in a specific language with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping("/language/{language}")
    public ResponseEntity<?> getBooksByLanguage(
            @Parameter(description = "Language code or name", required = true, example = "English")
            @PathVariable String language,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Search books by author",
            description = "Searches for books by author name (case-insensitive, partial match) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping("/search/author")
    public ResponseEntity<?> searchBooksByAuthor(
            @Parameter(description = "Author name search keyword", required = true, example = "Smith")
            @RequestParam String authorName,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Search books by genre",
            description = "Searches for books by genre name (case-insensitive, partial match) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping("/search/genre")
    public ResponseEntity<?> searchBooksByGenre(
            @Parameter(description = "Genre name search keyword", required = true, example = "Fiction")
            @RequestParam String genreName,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "General search for books",
            description = "Performs a comprehensive search across book titles and author names with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(
            @Parameter(description = "Search keyword (searches in title and author)", required = true, example = "Java Programming")
            @RequestParam String keyword,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
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
    @Operation(
            summary = "Check if ISBN exists",
            description = "Verifies whether a book with the specified ISBN already exists in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existence check completed successfully")
    })
    @GetMapping("/exists/isbn/{isbn}")
    public ResponseEntity<Boolean> checkIsbnExists(
            @Parameter(description = "Book ISBN to check", required = true, example = "978-0-13-468599-1")
            @PathVariable String isbn) {
        boolean exists = bookService.existsByIsbn(isbn);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Count books by status
    @Operation(
            summary = "Count books by status",
            description = "Returns the total count of books with the specified availability status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countBooksByStatus(
            @Parameter(description = "Book status", required = true, example = "AVAILABLE")
            @PathVariable BookStatus status) {
        long count = bookService.countByStatus(status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
