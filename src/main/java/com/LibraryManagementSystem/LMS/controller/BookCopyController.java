package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.BookCopyRequestDTO;
import com.LibraryManagementSystem.LMS.dto.BookCopyResponseDTO;
import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.enums.BookCopyStatus;
import com.LibraryManagementSystem.LMS.mapper.BookCopyMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.BookCopyService;
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
public class BookCopyController {
    
    private final BookCopyService bookCopyService;
    private final BookCopyMapper bookCopyMapper;
    
    public BookCopyController(BookCopyService bookCopyService, BookCopyMapper bookCopyMapper) {
        this.bookCopyService = bookCopyService;
        this.bookCopyMapper = bookCopyMapper;
    }
    
    // Create new BookCopy
    @PostMapping
    public ResponseEntity<BookCopyResponseDTO> createBookCopy(@Valid @RequestBody BookCopyRequestDTO requestDTO) {
        BookCopy bookCopy = bookCopyMapper.toEntity(requestDTO);
        BookCopy createdBookCopy = bookCopyService.create(bookCopy);
        BookCopyResponseDTO responseDTO = bookCopyMapper.toResponseDTO(createdBookCopy);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get BookCopy by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookCopyResponseDTO> getBookCopyById(@PathVariable Long id) {
        BookCopy bookCopy = bookCopyService.getById(id);
        BookCopyResponseDTO responseDTO = bookCopyMapper.toResponseDTO(bookCopy);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all BookCopies (with optional pagination)
    @GetMapping
    public ResponseEntity<?> getAllBookCopies(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
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
    @PutMapping("/{id}")
    public ResponseEntity<BookCopyResponseDTO> updateBookCopy(
            @PathVariable Long id,
            @Valid @RequestBody BookCopyRequestDTO requestDTO) {
        BookCopy bookCopy = bookCopyMapper.toEntity(requestDTO);
        BookCopy updatedBookCopy = bookCopyService.update(id, bookCopy);
        BookCopyResponseDTO responseDTO = bookCopyMapper.toResponseDTO(updatedBookCopy);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete BookCopy by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookCopy(@PathVariable Long id) {
        bookCopyService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get BookCopy by barcode
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<BookCopyResponseDTO> getBookCopyByBarcode(@PathVariable String barcode) {
        BookCopy bookCopy = bookCopyService.findByBarcode(barcode);
        BookCopyResponseDTO responseDTO = bookCopyMapper.toResponseDTO(bookCopy);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Check if barcode exists
    @GetMapping("/exists/barcode/{barcode}")
    public ResponseEntity<Boolean> checkBarcodeExists(@PathVariable String barcode) {
        boolean exists = bookCopyService.existsByBarcode(barcode);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Get all copies of a specific book
    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getBookCopiesByBookId(
            @PathVariable Long bookId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "barcode") String sortBy,
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
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getBookCopiesByStatus(
            @PathVariable BookCopyStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "barcode") String sortBy,
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
    
    // Get BookCopies by book ID and status
    @GetMapping("/book/{bookId}/status/{status}")
    public ResponseEntity<?> getBookCopiesByBookIdAndStatus(
            @PathVariable Long bookId,
            @PathVariable BookCopyStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "barcode") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BookCopy> bookCopyPage = bookCopyService.findByBookIdAndStatus(bookId, status, pageable);
            Page<BookCopyResponseDTO> responsePage = bookCopyPage.map(bookCopyMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<BookCopy> bookCopies = bookCopyService.findByBookIdAndStatus(bookId, status);
            List<BookCopyResponseDTO> responseDTOs = bookCopies.stream()
                    .map(bookCopyMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get available copies of a specific book
    @GetMapping("/book/{bookId}/available")
    public ResponseEntity<?> getAvailableCopiesByBookId(
            @PathVariable Long bookId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "barcode") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BookCopy> bookCopyPage = bookCopyService.findAvailableCopiesByBookId(bookId, pageable);
            Page<BookCopyResponseDTO> responsePage = bookCopyPage.map(bookCopyMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<BookCopy> bookCopies = bookCopyService.findAvailableCopiesByBookId(bookId);
            List<BookCopyResponseDTO> responseDTOs = bookCopies.stream()
                    .map(bookCopyMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get BookCopies by location
    @GetMapping("/location/{location}")
    public ResponseEntity<?> getBookCopiesByLocation(
            @PathVariable String location,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "barcode") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BookCopy> bookCopyPage = bookCopyService.findByLocation(location, pageable);
            Page<BookCopyResponseDTO> responsePage = bookCopyPage.map(bookCopyMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<BookCopy> bookCopies = bookCopyService.findByLocation(location);
            List<BookCopyResponseDTO> responseDTOs = bookCopies.stream()
                    .map(bookCopyMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get BookCopies by condition
    @GetMapping("/condition/{condition}")
    public ResponseEntity<?> getBookCopiesByCondition(
            @PathVariable String condition,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "barcode") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BookCopy> bookCopyPage = bookCopyService.findByCondition(condition, pageable);
            Page<BookCopyResponseDTO> responsePage = bookCopyPage.map(bookCopyMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<BookCopy> bookCopies = bookCopyService.findByCondition(condition);
            List<BookCopyResponseDTO> responseDTOs = bookCopies.stream()
                    .map(bookCopyMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get BookCopies by location and status
    @GetMapping("/location/{location}/status/{status}")
    public ResponseEntity<?> getBookCopiesByLocationAndStatus(
            @PathVariable String location,
            @PathVariable BookCopyStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "barcode") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BookCopy> bookCopyPage = bookCopyService.findByLocationAndStatus(location, status, pageable);
            Page<BookCopyResponseDTO> responsePage = bookCopyPage.map(bookCopyMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<BookCopy> bookCopies = bookCopyService.findByLocationAndStatus(location, status);
            List<BookCopyResponseDTO> responseDTOs = bookCopies.stream()
                    .map(bookCopyMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Count copies by book ID
    @GetMapping("/count/book/{bookId}")
    public ResponseEntity<Long> countBookCopiesByBookId(@PathVariable Long bookId) {
        long count = bookCopyService.countByBookId(bookId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    // Count copies by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countBookCopiesByStatus(@PathVariable BookCopyStatus status) {
        long count = bookCopyService.countByStatus(status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    
    // Count available copies by book ID
    @GetMapping("/count/book/{bookId}/available")
    public ResponseEntity<Long> countAvailableCopiesByBookId(@PathVariable Long bookId) {
        long count = bookCopyService.countAvailableCopiesByBookId(bookId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
