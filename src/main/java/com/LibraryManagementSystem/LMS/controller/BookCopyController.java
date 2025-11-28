package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.BookCopyRequestDTO;
import com.LibraryManagementSystem.LMS.dto.BookCopyResponseDTO;
import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.enums.BookCopyStatus;
import com.LibraryManagementSystem.LMS.mapper.BookCopyMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.BookCopyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book-copies")
@Tag(name = "Book Copies", description = "Book copy management APIs - Manage individual physical copies of books including barcodes, locations, conditions, and availability status")
public class BookCopyController {
    
    private final BookCopyService bookCopyService;
    private final BookCopyMapper bookCopyMapper;
    
    public BookCopyController(BookCopyService bookCopyService, BookCopyMapper bookCopyMapper) {
        this.bookCopyService = bookCopyService;
        this.bookCopyMapper = bookCopyMapper;
    }
    
    // Create new BookCopy
    @Operation(
            summary = "Create a new book copy",
            description = "Creates a new physical copy of a book with a unique barcode, location, and condition information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book copy created successfully",
                    content = @Content(schema = @Schema(implementation = BookCopyResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "409", description = "Book copy with the same barcode already exists")
    })
    @PostMapping
    public ResponseEntity<BookCopyResponseDTO> createBookCopy(
            @Parameter(description = "Book copy details to create", required = true)
            @Valid @RequestBody BookCopyRequestDTO requestDTO) {
        BookCopy bookCopy = bookCopyMapper.toEntity(requestDTO);
        BookCopy createdBookCopy = bookCopyService.create(bookCopy);
        BookCopyResponseDTO responseDTO = bookCopyMapper.toResponseDTO(createdBookCopy);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get BookCopy by ID
    @Operation(
            summary = "Get book copy by ID",
            description = "Retrieves a specific book copy's details by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book copy found successfully",
                    content = @Content(schema = @Schema(implementation = BookCopyResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book copy not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookCopyResponseDTO> getBookCopyById(
            @Parameter(description = "Book copy ID", required = true, example = "1")
            @PathVariable Long id) {
        BookCopy bookCopy = bookCopyService.getById(id);
        BookCopyResponseDTO responseDTO = bookCopyMapper.toResponseDTO(bookCopy);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all BookCopies (with optional pagination)
    @Operation(
            summary = "Get all book copies",
            description = "Retrieves all book copies in the system with optional pagination and sorting"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book copies retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<?> getAllBookCopies(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "barcode")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BookCopy> bookCopyPage = bookCopyService.getAllPaginated(pageable);
            Page<BookCopyResponseDTO> responsePage = bookCopyPage.map(bookCopyMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<BookCopy> bookCopies = bookCopyService.getAll();
            List<BookCopyResponseDTO> responseDTOs = bookCopies.stream()
                    .map(bookCopyMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Update BookCopy by ID
    @Operation(
            summary = "Update book copy",
            description = "Updates an existing book copy's information including location, condition, and status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book copy updated successfully",
                    content = @Content(schema = @Schema(implementation = BookCopyResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "Book copy not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Barcode conflicts with existing book copy")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookCopyResponseDTO> updateBookCopy(
            @Parameter(description = "Book copy ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated book copy details", required = true)
            @Valid @RequestBody BookCopyRequestDTO requestDTO) {
        BookCopy bookCopy = bookCopyMapper.toEntity(requestDTO);
        BookCopy updatedBookCopy = bookCopyService.update(id, bookCopy);
        BookCopyResponseDTO responseDTO = bookCopyMapper.toResponseDTO(updatedBookCopy);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete BookCopy by ID
    @Operation(
            summary = "Delete book copy",
            description = "Permanently deletes a book copy from the system. Note: Cannot delete copies with active borrows."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book copy deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book copy not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Cannot delete book copy - active borrows exist")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookCopy(
            @Parameter(description = "Book copy ID", required = true, example = "1")
            @PathVariable Long id) {
        bookCopyService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get BookCopy by barcode
    @Operation(
            summary = "Get book copy by barcode",
            description = "Retrieves a specific book copy by its unique barcode identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book copy found successfully",
                    content = @Content(schema = @Schema(implementation = BookCopyResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Book copy not found with the given barcode")
    })
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<BookCopyResponseDTO> getBookCopyByBarcode(
            @Parameter(description = "Book copy barcode", required = true, example = "BC-001-12345")
            @PathVariable String barcode) {
        BookCopy bookCopy = bookCopyService.findByBarcode(barcode);
        BookCopyResponseDTO responseDTO = bookCopyMapper.toResponseDTO(bookCopy);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all copies of a specific book
    @Operation(
            summary = "Get all copies of a specific book",
            description = "Retrieves all physical copies associated with a specific book with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book copies retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found with the given ID")
    })
    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getBookCopiesByBookId(
            @Parameter(description = "Book ID", required = true, example = "1")
            @PathVariable Long bookId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "barcode")
            @RequestParam(defaultValue = "barcode") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BookCopy> bookCopyPage = bookCopyService.findByBookId(bookId, pageable);
            Page<BookCopyResponseDTO> responsePage = bookCopyPage.map(bookCopyMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<BookCopy> bookCopies = bookCopyService.findByBookId(bookId);
            List<BookCopyResponseDTO> responseDTOs = bookCopies.stream()
                    .map(bookCopyMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get BookCopies by status
    @Operation(
            summary = "Get book copies by status",
            description = "Filters book copies by their current status (AVAILABLE, BORROWED, RESERVED, etc.) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book copies retrieved successfully")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBookCopiesByStatus(
            @Parameter(description = "Book copy status", required = true, example = "AVAILABLE")
            @PathVariable BookCopyStatus status,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "barcode")
            @RequestParam(defaultValue = "barcode") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BookCopy> bookCopyPage = bookCopyService.findByStatus(status, pageable);
            Page<BookCopyResponseDTO> responsePage = bookCopyPage.map(bookCopyMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<BookCopy> bookCopies = bookCopyService.findByStatus(status);
            List<BookCopyResponseDTO> responseDTOs = bookCopies.stream()
                    .map(bookCopyMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
}
