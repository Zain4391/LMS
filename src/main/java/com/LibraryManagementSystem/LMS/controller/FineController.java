package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.FineRequestDTO;
import com.LibraryManagementSystem.LMS.dto.FineResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.enums.FineStatus;
import com.LibraryManagementSystem.LMS.mapper.FineMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.FineService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fines")
public class FineController {
    
    private final FineService fineService;
    private final FineMapper fineMapper;
    
    public FineController(FineService fineService, FineMapper fineMapper) {
        this.fineService = fineService;
        this.fineMapper = fineMapper;
    }
    
    // Create new Fine
    @PostMapping
    public ResponseEntity<FineResponseDTO> createFine(@Valid @RequestBody FineRequestDTO requestDTO) {
        Fine fine = fineMapper.toEntity(requestDTO);
        Fine createdFine = fineService.create(fine);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(createdFine);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Fine by ID
    @GetMapping("/{id}")
    public ResponseEntity<FineResponseDTO> getFineById(@PathVariable Long id) {
        Fine fine = fineService.getById(id);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(fine);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all Fines (with optional pagination)
    @GetMapping
    public ResponseEntity<?> getAllFines(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "assessedDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Fine> finePage = fineService.getAllPaginated(pageable);
            Page<FineResponseDTO> responsePage = finePage.map(fineMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Fine> fineList = fineService.getAll();
            List<FineResponseDTO> responseDTOs = fineList.stream()
                    .map(fineMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Update Fine by ID
    @PutMapping("/{id}")
    public ResponseEntity<FineResponseDTO> updateFine(
            @PathVariable Long id,
            @Valid @RequestBody FineRequestDTO requestDTO) {
        Fine fine = fineMapper.toEntity(requestDTO);
        Fine updatedFine = fineService.update(id, fine);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(updatedFine);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete Fine by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        fineService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get Fine by Borrowed ID
    @GetMapping("/borrowed/{borrowedId}")
    public ResponseEntity<FineResponseDTO> getFineByBorrowedId(@PathVariable Long borrowedId) {
        Fine fine = fineService.findByBorrowedId(borrowedId);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(fine);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get Fines by Status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getFinesByStatus(
            @PathVariable FineStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "assessedDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Fine> finePage = fineService.findByStatus(status, pageable);
            Page<FineResponseDTO> responsePage = finePage.map(fineMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Fine> fineList = fineService.findByStatus(status);
            List<FineResponseDTO> responseDTOs = fineList.stream()
                    .map(fineMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Fines by User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFinesByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "assessedDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Fine> finePage = fineService.findByUserId(userId, pageable);
            Page<FineResponseDTO> responsePage = finePage.map(fineMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Fine> fineList = fineService.findByUserId(userId);
            List<FineResponseDTO> responseDTOs = fineList.stream()
                    .map(fineMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Pending Fines by User ID
    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<?> getPendingFinesByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "assessedDate") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Fine> finePage = fineService.findPendingFinesByUserId(userId, pageable);
            Page<FineResponseDTO> responsePage = finePage.map(fineMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Fine> fineList = fineService.findPendingFinesByUserId(userId);
            List<FineResponseDTO> responseDTOs = fineList.stream()
                    .map(fineMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Fines by Assessed Date Range
    @GetMapping("/search/assessed-date")
    public ResponseEntity<?> getFinesByAssessedDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "assessedDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Fine> finePage = fineService.findByAssessedDateBetween(startDate, endDate, pageable);
            Page<FineResponseDTO> responsePage = finePage.map(fineMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Fine> fineList = fineService.findByAssessedDateBetween(startDate, endDate);
            List<FineResponseDTO> responseDTOs = fineList.stream()
                    .map(fineMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Fines Greater Than Amount
    @GetMapping("/search/amount")
    public ResponseEntity<?> getFinesByAmountGreaterThan(
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "amount") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Fine> finePage = fineService.findByAmountGreaterThan(amount, pageable);
            Page<FineResponseDTO> responsePage = finePage.map(fineMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Fine> fineList = fineService.findByAmountGreaterThan(amount);
            List<FineResponseDTO> responseDTOs = fineList.stream()
                    .map(fineMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Pay a Fine
    @PostMapping("/{id}/pay")
    public ResponseEntity<FineResponseDTO> payFine(@PathVariable Long id) {
        Fine paidFine = fineService.payFine(id);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(paidFine);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Waive a Fine
    @PostMapping("/{id}/waive")
    public ResponseEntity<FineResponseDTO> waiveFine(@PathVariable Long id) {
        Fine waivedFine = fineService.waiveFine(id);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(waivedFine);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Assess Fine for Overdue Book
    @PostMapping("/assess/borrowed/{borrowedId}")
    public ResponseEntity<FineResponseDTO> assessFineForOverdue(
            @PathVariable Long borrowedId,
            @RequestParam(defaultValue = "1.00") BigDecimal dailyRate) {
        Fine assessedFine = fineService.assessFineForOverdue(borrowedId, dailyRate);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(assessedFine);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Total Pending Fines by User
    @GetMapping("/user/{userId}/total-pending")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPendingFinesByUserId(@PathVariable Long userId) {
        BigDecimal total = fineService.calculateTotalPendingFinesByUserId(userId);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("totalPending", total);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Get Total Fines by Status
    @GetMapping("/total/status/{status}")
    public ResponseEntity<Map<String, BigDecimal>> getTotalFinesByStatus(@PathVariable FineStatus status) {
        BigDecimal total = fineService.calculateTotalFinesByStatus(status);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("total", total);
        response.put("status", new BigDecimal(status.name().length())); // Just for map consistency
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Count Fines by Status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Map<String, Long>> countFinesByStatus(@PathVariable FineStatus status) {
        long count = fineService.countByStatus(status);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if Fine Exists for Borrowed Record
    @GetMapping("/exists/borrowed/{borrowedId}")
    public ResponseEntity<Map<String, Boolean>> checkFineExistsByBorrowedId(@PathVariable Long borrowedId) {
        boolean exists = fineService.existsByBorrowedId(borrowedId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if User Has Pending Fines
    @GetMapping("/user/{userId}/has-pending")
    public ResponseEntity<Map<String, Boolean>> checkUserHasPendingFines(@PathVariable Long userId) {
        boolean hasPending = fineService.hasUserPendingFines(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasPendingFines", hasPending);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

