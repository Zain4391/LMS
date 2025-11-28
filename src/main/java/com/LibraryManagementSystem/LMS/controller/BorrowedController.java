package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.BorrowedRequestDTO;
import com.LibraryManagementSystem.LMS.dto.BorrowedResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.enums.BorrowStatus;
import com.LibraryManagementSystem.LMS.mapper.BorrowedMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.BorrowedService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/borrowed")
@Tag(name = "Borrowed Records", description = "Borrowed record management APIs - Track book borrowing transactions, due dates, returns, and overdue status")
public class BorrowedController {
    
    private final BorrowedService borrowedService;
    private final BorrowedMapper borrowedMapper;
    
    public BorrowedController(BorrowedService borrowedService, BorrowedMapper borrowedMapper) {
        this.borrowedService = borrowedService;
        this.borrowedMapper = borrowedMapper;
    }
    
    // Create new Borrowed record (Borrow a book)
    @Operation(
            summary = "Create a borrowed record (borrow a book)",
            description = "Creates a new borrow transaction when a user borrows a book copy. Automatically sets borrow date and calculates due date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Borrow record created successfully",
                    content = @Content(schema = @Schema(implementation = BorrowedResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed or user cannot borrow"),
            @ApiResponse(responseCode = "409", description = "Book copy is not available for borrowing")
    })
    @PostMapping
    public ResponseEntity<BorrowedResponseDTO> createBorrowed(
            @Parameter(description = "Borrow record details to create", required = true)
            @Valid @RequestBody BorrowedRequestDTO requestDTO) {
        Borrowed borrowed = borrowedMapper.toEntity(requestDTO);
        Borrowed createdBorrowed = borrowedService.create(borrowed);
        BorrowedResponseDTO responseDTO = borrowedMapper.toResponseDTO(createdBorrowed);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Borrowed record by ID
    @Operation(
            summary = "Get borrowed record by ID",
            description = "Retrieves a specific borrow transaction's details by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrow record found successfully",
                    content = @Content(schema = @Schema(implementation = BorrowedResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Borrow record not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BorrowedResponseDTO> getBorrowedById(
            @Parameter(description = "Borrowed record ID", required = true, example = "1")
            @PathVariable Long id) {
        Borrowed borrowed = borrowedService.getById(id);
        BorrowedResponseDTO responseDTO = borrowedMapper.toResponseDTO(borrowed);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all Borrowed records (with optional pagination)
    @Operation(
            summary = "Get all borrowed records",
            description = "Retrieves all borrow transactions in the system with optional pagination and sorting"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrow records retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<?> getAllBorrowed(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "borrowDate")
            @RequestParam(defaultValue = "borrowDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.getAllPaginated(pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.getAll();
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Update Borrowed record by ID
    @Operation(
            summary = "Update borrowed record",
            description = "Updates an existing borrow transaction's details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrowed record updated successfully",
                    content = @Content(schema = @Schema(implementation = BorrowedResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "Borrowed record not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BorrowedResponseDTO> updateBorrowed(
            @Parameter(description = "Borrowed record ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated borrowed record details", required = true)
            @Valid @RequestBody BorrowedRequestDTO requestDTO) {
        Borrowed borrowed = borrowedMapper.toEntity(requestDTO);
        Borrowed updatedBorrowed = borrowedService.update(id, borrowed);
        BorrowedResponseDTO responseDTO = borrowedMapper.toResponseDTO(updatedBorrowed);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete Borrowed record by ID
    @Operation(
            summary = "Delete borrowed record",
            description = "Permanently deletes a borrow transaction from the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Borrowed record deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Borrowed record not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowed(
            @Parameter(description = "Borrowed record ID", required = true, example = "1")
            @PathVariable Long id) {
        borrowedService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Return a book
    @Operation(
            summary = "Return a borrowed book",
            description = "Processes the return of a borrowed book. If return date is not provided, uses current date. Updates status and may assess fines for late returns."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully",
                    content = @Content(schema = @Schema(implementation = BorrowedResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Book has already been returned"),
            @ApiResponse(responseCode = "404", description = "Borrow record not found")
    })
    @PostMapping("/{id}/return")
    public ResponseEntity<BorrowedResponseDTO> returnBook(
            @Parameter(description = "Borrowed record ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Return date (defaults to current date)", example = "2024-12-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        LocalDate actualReturnDate = returnDate != null ? returnDate : LocalDate.now();
        Borrowed returned = borrowedService.returnBook(id, actualReturnDate);
        BorrowedResponseDTO responseDTO = borrowedMapper.toResponseDTO(returned);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get borrowed records by User ID
    @Operation(
            summary = "Get borrowed records by user",
            description = "Retrieves all borrow transactions for a specific user with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrow records retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBorrowedByUserId(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "borrowDate")
            @RequestParam(defaultValue = "borrowDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.findByUserId(userId, pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.findByUserId(userId);
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get borrowed records by BookCopy ID
    @Operation(
            summary = "Get borrowed records by book copy",
            description = "Retrieves all borrow transactions for a specific book copy with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrow records retrieved successfully")
    })
    @GetMapping("/book-copy/{bookCopyId}")
    public ResponseEntity<?> getBorrowedByBookCopyId(
            @Parameter(description = "Book copy ID", required = true, example = "1")
            @PathVariable Long bookCopyId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "borrowDate")
            @RequestParam(defaultValue = "borrowDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.findByBookCopyId(bookCopyId, pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.findByBookCopyId(bookCopyId);
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get borrowed records by Status
    @Operation(
            summary = "Get borrowed records by status",
            description = "Retrieves all borrow transactions with a specific status (BORROWED, RETURNED, OVERDUE) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrow records retrieved successfully")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBorrowedByStatus(
            @Parameter(description = "Borrow status", required = true, example = "BORROWED")
            @PathVariable BorrowStatus status,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "borrowDate")
            @RequestParam(defaultValue = "borrowDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.findByStatus(status, pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.findByStatus(status);
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get borrowed records by User ID and Status
    @Operation(
            summary = "Get borrowed records by user and status",
            description = "Retrieves borrow transactions filtered by both user ID and status with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrow records retrieved successfully")
    })
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<?> getBorrowedByUserIdAndStatus(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Borrow status", required = true, example = "BORROWED")
            @PathVariable BorrowStatus status,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "borrowDate")
            @RequestParam(defaultValue = "borrowDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.findByUserIdAndStatus(userId, status, pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.findByUserIdAndStatus(userId, status);
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get active borrows by User ID
    @Operation(
            summary = "Get active borrows by user",
            description = "Retrieves all currently active (not returned) borrow transactions for a specific user with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active borrow records retrieved successfully")
    })
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<?> getActiveBorrowsByUserId(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "borrowDate")
            @RequestParam(defaultValue = "borrowDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.findActiveBorrowsByUserId(userId, pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.findActiveBorrowsByUserId(userId);
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get all overdue borrows
    @Operation(
            summary = "Get all overdue borrows",
            description = "Retrieves all borrow transactions that are past their due date and not yet returned with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overdue borrow records retrieved successfully")
    })
    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueBorrows(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "dueDate")
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.findOverdueBorrows(pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.findOverdueBorrows();
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get overdue borrows by User ID
    @Operation(
            summary = "Get overdue borrows by user",
            description = "Retrieves all overdue borrow transactions for a specific user with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overdue borrow records retrieved successfully")
    })
    @GetMapping("/user/{userId}/overdue")
    public ResponseEntity<?> getOverdueBorrowsByUserId(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "dueDate")
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.findOverdueBorrowsByUserId(userId, pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.findOverdueBorrowsByUserId(userId);
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get borrowed records by borrow date range
    @Operation(
            summary = "Get borrowed records by borrow date range",
            description = "Retrieves borrow transactions within a specified date range with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrow records retrieved successfully")
    })
    @GetMapping("/search/borrow-date")
    public ResponseEntity<?> getBorrowedByBorrowDateRange(
            @Parameter(description = "Start date", required = true, example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2024-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "borrowDate")
            @RequestParam(defaultValue = "borrowDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.findByBorrowDateBetween(startDate, endDate, pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.findByBorrowDateBetween(startDate, endDate);
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get borrowed records by due date range
    @Operation(
            summary = "Get borrowed records by due date range",
            description = "Retrieves borrow transactions with due dates within a specified range with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrow records retrieved successfully")
    })
    @GetMapping("/search/due-date")
    public ResponseEntity<?> getBorrowedByDueDateRange(
            @Parameter(description = "Start date", required = true, example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2024-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "dueDate")
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.findByDueDateBetween(startDate, endDate, pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.findByDueDateBetween(startDate, endDate);
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get borrowed records by return date range
    @Operation(
            summary = "Get borrowed records by return date range",
            description = "Retrieves returned borrow transactions with return dates within a specified range with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrow records retrieved successfully")
    })
    @GetMapping("/search/return-date")
    public ResponseEntity<?> getBorrowedByReturnDateRange(
            @Parameter(description = "Start date", required = true, example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true, example = "2024-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "returnDate")
            @RequestParam(defaultValue = "returnDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Borrowed> borrowedPage = borrowedService.findByReturnDateBetween(startDate, endDate, pageable);
            Page<BorrowedResponseDTO> responsePage = borrowedPage.map(borrowedMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Borrowed> borrowedList = borrowedService.findByReturnDateBetween(startDate, endDate);
            List<BorrowedResponseDTO> responseDTOs = borrowedList.stream()
                    .map(borrowedMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Mark all overdue records
    @Operation(
            summary = "Mark all overdue records",
            description = "Batch updates all overdue borrow records to OVERDUE status. Typically run as a scheduled job."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overdue records updated successfully")
    })
    @PostMapping("/mark-overdue")
    public ResponseEntity<Map<String, String>> markOverdue() {
        borrowedService.markOverdue();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Overdue records have been updated");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if book copy is available
    @Operation(
            summary = "Check if book copy is available",
            description = "Validates whether a specific book copy is currently available for borrowing"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability check completed successfully")
    })
    @GetMapping("/book-copy/{bookCopyId}/available")
    public ResponseEntity<Map<String, Boolean>> isBookCopyAvailable(
            @Parameter(description = "Book copy ID", required = true, example = "1")
            @PathVariable Long bookCopyId) {
        boolean available = borrowedService.isBookCopyAvailable(bookCopyId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if user has exceeded borrow limit
    @Operation(
            summary = "Check if user exceeded borrow limit",
            description = "Validates whether a user has exceeded the maximum number of concurrent active borrows"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Limit check completed successfully")
    })
    @GetMapping("/user/{userId}/limit-check")
    public ResponseEntity<Map<String, Boolean>> checkBorrowLimit(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Maximum borrow limit", example = "5")
            @RequestParam(defaultValue = "5") int limit) {
        boolean exceeded = borrowedService.hasUserExceededBorrowLimit(userId, limit);
        Map<String, Boolean> response = new HashMap<>();
        response.put("limitExceeded", exceeded);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Count active borrows by user
    @Operation(
            summary = "Count active borrows by user",
            description = "Returns the number of currently active (not returned) borrows for a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    @GetMapping("/user/{userId}/active-count")
    public ResponseEntity<Map<String, Integer>> countActiveBorrows(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId) {
        int count = borrowedService.countActiveBorrowsByUserId(userId);
        Map<String, Integer> response = new HashMap<>();
        response.put("activeCount", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
