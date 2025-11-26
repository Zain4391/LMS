package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.BorrowedRequestDTO;
import com.LibraryManagementSystem.LMS.dto.BorrowedResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.enums.BorrowStatus;
import com.LibraryManagementSystem.LMS.mapper.BorrowedMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.BorrowedService;
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
public class BorrowedController {
    
    private final BorrowedService borrowedService;
    private final BorrowedMapper borrowedMapper;
    
    public BorrowedController(BorrowedService borrowedService, BorrowedMapper borrowedMapper) {
        this.borrowedService = borrowedService;
        this.borrowedMapper = borrowedMapper;
    }
    
    // Create new Borrowed record (Borrow a book)
    @PostMapping
    public ResponseEntity<BorrowedResponseDTO> createBorrowed(@Valid @RequestBody BorrowedRequestDTO requestDTO) {
        Borrowed borrowed = borrowedMapper.toEntity(requestDTO);
        Borrowed createdBorrowed = borrowedService.create(borrowed);
        BorrowedResponseDTO responseDTO = borrowedMapper.toResponseDTO(createdBorrowed);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Borrowed record by ID
    @GetMapping("/{id}")
    public ResponseEntity<BorrowedResponseDTO> getBorrowedById(@PathVariable Long id) {
        Borrowed borrowed = borrowedService.getById(id);
        BorrowedResponseDTO responseDTO = borrowedMapper.toResponseDTO(borrowed);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all Borrowed records (with optional pagination)
    @GetMapping
    public ResponseEntity<?> getAllBorrowed(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "borrowDate") String sortBy,
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
    @PutMapping("/{id}")
    public ResponseEntity<BorrowedResponseDTO> updateBorrowed(
            @PathVariable Long id,
            @Valid @RequestBody BorrowedRequestDTO requestDTO) {
        Borrowed borrowed = borrowedMapper.toEntity(requestDTO);
        Borrowed updatedBorrowed = borrowedService.update(id, borrowed);
        BorrowedResponseDTO responseDTO = borrowedMapper.toResponseDTO(updatedBorrowed);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete Borrowed record by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowed(@PathVariable Long id) {
        borrowedService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Return a book
    @PostMapping("/{id}/return")
    public ResponseEntity<BorrowedResponseDTO> returnBook(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        LocalDate actualReturnDate = returnDate != null ? returnDate : LocalDate.now();
        Borrowed returned = borrowedService.returnBook(id, actualReturnDate);
        BorrowedResponseDTO responseDTO = borrowedMapper.toResponseDTO(returned);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get borrowed records by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBorrowedByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "borrowDate") String sortBy,
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
    @GetMapping("/book-copy/{bookCopyId}")
    public ResponseEntity<?> getBorrowedByBookCopyId(
            @PathVariable Long bookCopyId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "borrowDate") String sortBy,
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
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBorrowedByStatus(
            @PathVariable BorrowStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "borrowDate") String sortBy,
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
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<?> getBorrowedByUserIdAndStatus(
            @PathVariable Long userId,
            @PathVariable BorrowStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "borrowDate") String sortBy,
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
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<?> getActiveBorrowsByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "borrowDate") String sortBy,
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
    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueBorrows(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
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
    @GetMapping("/user/{userId}/overdue")
    public ResponseEntity<?> getOverdueBorrowsByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
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
    @GetMapping("/search/borrow-date")
    public ResponseEntity<?> getBorrowedByBorrowDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "borrowDate") String sortBy,
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
    @GetMapping("/search/due-date")
    public ResponseEntity<?> getBorrowedByDueDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
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
    @GetMapping("/search/return-date")
    public ResponseEntity<?> getBorrowedByReturnDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "returnDate") String sortBy,
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
    @PostMapping("/mark-overdue")
    public ResponseEntity<Map<String, String>> markOverdue() {
        borrowedService.markOverdue();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Overdue records have been updated");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if book copy is available
    @GetMapping("/book-copy/{bookCopyId}/available")
    public ResponseEntity<Map<String, Boolean>> isBookCopyAvailable(@PathVariable Long bookCopyId) {
        boolean available = borrowedService.isBookCopyAvailable(bookCopyId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if user has exceeded borrow limit
    @GetMapping("/user/{userId}/limit-check")
    public ResponseEntity<Map<String, Boolean>> checkBorrowLimit(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit) {
        boolean exceeded = borrowedService.hasUserExceededBorrowLimit(userId, limit);
        Map<String, Boolean> response = new HashMap<>();
        response.put("limitExceeded", exceeded);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Count active borrows by user
    @GetMapping("/user/{userId}/active-count")
    public ResponseEntity<Map<String, Integer>> countActiveBorrows(@PathVariable Long userId) {
        int count = borrowedService.countActiveBorrowsByUserId(userId);
        Map<String, Integer> response = new HashMap<>();
        response.put("activeCount", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
