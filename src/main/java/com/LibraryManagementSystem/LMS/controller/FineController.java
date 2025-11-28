package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.FineRequestDTO;
import com.LibraryManagementSystem.LMS.dto.FineResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.enums.FineStatus;
import com.LibraryManagementSystem.LMS.mapper.FineMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.FineService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fines")
@Tag(name = "Fines", description = "Fine management APIs - Manage library fines for overdue books, track payments, and calculate penalties")
public class FineController {
    
    private final FineService fineService;
    private final FineMapper fineMapper;
    
    public FineController(FineService fineService, FineMapper fineMapper) {
        this.fineService = fineService;
        this.fineMapper = fineMapper;
    }
    
    // Create new Fine
    @Operation(
            summary = "Create a new fine",
            description = "Creates a new fine record for a user, typically associated with an overdue book return"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fine created successfully",
                    content = @Content(schema = @Schema(implementation = FineResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "409", description = "Fine already exists for this borrow record")
    })
    @PostMapping
    public ResponseEntity<FineResponseDTO> createFine(
            @Parameter(description = "Fine details to create", required = true)
            @Valid @RequestBody FineRequestDTO requestDTO) {
        Fine fine = fineMapper.toEntity(requestDTO);
        Fine createdFine = fineService.create(fine);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(createdFine);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Fine by ID
    @Operation(
            summary = "Get fine by ID",
            description = "Retrieves a specific fine's details by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fine found successfully",
                    content = @Content(schema = @Schema(implementation = FineResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Fine not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FineResponseDTO> getFineById(
            @Parameter(description = "Fine ID", required = true, example = "1")
            @PathVariable Long id) {
        Fine fine = fineService.getById(id);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(fine);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all Fines (with optional pagination)
    @Operation(
            summary = "Get all fines",
            description = "Retrieves all fines with optional pagination and sorting"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fines retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<?> getAllFines(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "assessedDate")
            @RequestParam(defaultValue = "assessedDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    @Operation(
            summary = "Update fine by ID",
            description = "Updates an existing fine's details by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fine updated successfully",
                    content = @Content(schema = @Schema(implementation = FineResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data - validation failed"),
            @ApiResponse(responseCode = "404", description = "Fine not found with the given ID")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FineResponseDTO> updateFine(
            @Parameter(description = "Fine ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated fine details", required = true)
            @Valid @RequestBody FineRequestDTO requestDTO) {
        Fine fine = fineMapper.toEntity(requestDTO);
        Fine updatedFine = fineService.update(id, fine);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(updatedFine);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete Fine by ID
    @Operation(
            summary = "Delete fine by ID",
            description = "Permanently deletes a fine record by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fine deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Fine not found with the given ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFine(
            @Parameter(description = "Fine ID", required = true, example = "1")
            @PathVariable Long id) {
        fineService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get Fine by Borrowed ID
    @Operation(
            summary = "Get fine by borrowed record ID",
            description = "Retrieves a fine associated with a specific borrow transaction"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fine found successfully",
                    content = @Content(schema = @Schema(implementation = FineResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Fine not found for the given borrow record")
    })
    @GetMapping("/borrowed/{borrowedId}")
    public ResponseEntity<FineResponseDTO> getFineByBorrowedId(
            @Parameter(description = "Borrowed record ID", required = true, example = "1")
            @PathVariable Long borrowedId) {
        Fine fine = fineService.findByBorrowedId(borrowedId);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(fine);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get Fines by Status
    @Operation(
            summary = "Get fines by status",
            description = "Retrieves all fines with a specific status (PENDING, PAID, WAIVED) with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fines retrieved successfully")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getFinesByStatus(
            @Parameter(description = "Fine status", required = true, example = "PENDING")
            @PathVariable FineStatus status,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "assessedDate")
            @RequestParam(defaultValue = "assessedDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    @Operation(
            summary = "Get fines by user",
            description = "Retrieves all fines for a specific user with optional pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fines retrieved successfully")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFinesByUserId(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "assessedDate")
            @RequestParam(defaultValue = "assessedDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
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
    
    // Pay a Fine
    @Operation(
            summary = "Pay a fine",
            description = "Marks a fine as paid and updates its status to PAID. Creates a payment record."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fine paid successfully",
                    content = @Content(schema = @Schema(implementation = FineResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Fine has already been paid or waived"),
            @ApiResponse(responseCode = "404", description = "Fine not found")
    })
    @PostMapping("/{id}/pay")
    public ResponseEntity<FineResponseDTO> payFine(
            @Parameter(description = "Fine ID", required = true, example = "1")
            @PathVariable Long id) {
        Fine paidFine = fineService.payFine(id);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(paidFine);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Assess Fine for Overdue Book
    @Operation(
            summary = "Assess fine for overdue book",
            description = "Automatically calculates and creates a fine for an overdue book based on the number of days overdue and daily rate"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fine assessed successfully",
                    content = @Content(schema = @Schema(implementation = FineResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Book is not overdue or fine already exists"),
            @ApiResponse(responseCode = "404", description = "Borrow record not found")
    })
    @PostMapping("/assess/borrowed/{borrowedId}")
    public ResponseEntity<FineResponseDTO> assessFineForOverdue(
            @Parameter(description = "Borrowed record ID", required = true, example = "1")
            @PathVariable Long borrowedId,
            @Parameter(description = "Daily fine rate", example = "1.00")
            @RequestParam(defaultValue = "1.00") BigDecimal dailyRate) {
        Fine assessedFine = fineService.assessFineForOverdue(borrowedId, dailyRate);
        FineResponseDTO responseDTO = fineMapper.toResponseDTO(assessedFine);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
}

