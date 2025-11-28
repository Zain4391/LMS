package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.LibrarianRequestDTO;
import com.LibraryManagementSystem.LMS.dto.LibrarianResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Librarian;
import com.LibraryManagementSystem.LMS.enums.Role;
import com.LibraryManagementSystem.LMS.enums.Status;
import com.LibraryManagementSystem.LMS.mapper.LibrarianMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.LibrarianService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/librarians")
@Tag(name = "Librarians", description = "Librarian management APIs - Manage library staff accounts with role-based access control")
public class LibrarianController {
    
    private final LibrarianService librarianService;
    private final LibrarianMapper librarianMapper;
    
    public LibrarianController(LibrarianService librarianService, LibrarianMapper librarianMapper) {
        this.librarianService = librarianService;
        this.librarianMapper = librarianMapper;
    }
    
    // Create new Librarian
    @Operation(
            summary = "Create a new librarian",
            description = "Creates a new librarian staff account. Email and phone number must be unique. Password is automatically encrypted. Default role is STAFF and status is ACTIVE."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Librarian created successfully",
                    content = @Content(schema = @Schema(implementation = LibrarianResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input - Validation failed"),
            @ApiResponse(responseCode = "409", description = "Conflict - Email or phone number already exists")
    })
    @PostMapping
    public ResponseEntity<LibrarianResponseDTO> createLibrarian(
            @Parameter(description = "Librarian details for registration", required = true)
            @Valid @RequestBody LibrarianRequestDTO requestDTO) {
        Librarian librarian = librarianMapper.toEntity(requestDTO);
        Librarian createdLibrarian = librarianService.create(librarian);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(createdLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Librarian by ID
    @Operation(
            summary = "Get librarian by ID",
            description = "Retrieves a librarian's details by their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarian found successfully",
                    content = @Content(schema = @Schema(implementation = LibrarianResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Librarian not found with the given ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LibrarianResponseDTO> getLibrarianById(
            @Parameter(description = "Librarian ID", required = true, example = "1")
            @PathVariable Long id) {
        Librarian librarian = librarianService.getById(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(librarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all Librarians (with optional pagination)
    @Operation(
            summary = "Get all librarians",
            description = "Retrieves all librarians. Supports optional pagination and sorting. Without page/size parameters, returns complete list."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarians retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<?> getAllLibrarians(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Librarian> librarianPage = librarianService.getAllPaginated(pageable);
            Page<LibrarianResponseDTO> responsePage = librarianPage.map(librarianMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Librarian> librarianList = librarianService.getAll();
            List<LibrarianResponseDTO> responseDTOs = librarianList.stream()
                    .map(librarianMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Update Librarian by ID
    @Operation(
            summary = "Update librarian by ID",
            description = "Updates an existing librarian's information. Email and phone number must be unique if changed."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarian updated successfully",
                    content = @Content(schema = @Schema(implementation = LibrarianResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input - Validation failed"),
            @ApiResponse(responseCode = "404", description = "Librarian not found with the given ID"),
            @ApiResponse(responseCode = "409", description = "Conflict - Email or phone number already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LibrarianResponseDTO> updateLibrarian(
            @Parameter(description = "Librarian ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated librarian details", required = true)
            @Valid @RequestBody LibrarianRequestDTO requestDTO) {
        Librarian librarian = librarianMapper.toEntity(requestDTO);
        Librarian updatedLibrarian = librarianService.update(id, librarian);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(updatedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete Librarian by ID
    @Operation(
            summary = "Delete librarian by ID",
            description = "Permanently deletes a librarian from the system. This action cannot be undone."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Librarian deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Librarian not found with the given ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrarian(
            @Parameter(description = "Librarian ID", required = true, example = "1")
            @PathVariable Long id) {
        librarianService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get Librarian by Email
    @Operation(
            summary = "Get librarian by email",
            description = "Retrieves a librarian's details by their email address. Useful for authentication and profile lookup."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarian found successfully",
                    content = @Content(schema = @Schema(implementation = LibrarianResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Librarian not found with the given email")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<LibrarianResponseDTO> getLibrarianByEmail(
            @Parameter(description = "Librarian email address", required = true, example = "jane.smith@library.com")
            @PathVariable String email) {
        Librarian librarian = librarianService.findByEmail(email);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(librarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get Librarians by Role
    @Operation(
            summary = "Get librarians by role",
            description = "Retrieves all librarians with a specific role (ADMIN or STAFF). Supports pagination and sorting."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarians retrieved successfully")
    })
    @GetMapping("/role/{role}")
    public ResponseEntity<?> getLibrariansByRole(
            @Parameter(description = "Librarian role (STAFF/ADMIN)", required = true, example = "STAFF")
            @PathVariable Role role,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Librarian> librarianPage = librarianService.findByRole(role, pageable);
            Page<LibrarianResponseDTO> responsePage = librarianPage.map(librarianMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Librarian> librarianList = librarianService.findByRole(role);
            List<LibrarianResponseDTO> responseDTOs = librarianList.stream()
                    .map(librarianMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Librarians by Status
    @Operation(
            summary = "Get librarians by status",
            description = "Retrieves all librarians with a specific status (ACTIVE, INACTIVE, SUSPENDED). Supports pagination and sorting."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarians retrieved successfully")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getLibrariansByStatus(
            @Parameter(description = "Librarian status (ACTIVE/INACTIVE)", required = true, example = "ACTIVE")
            @PathVariable Status status,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Librarian> librarianPage = librarianService.findByStatus(status, pageable);
            Page<LibrarianResponseDTO> responsePage = librarianPage.map(librarianMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Librarian> librarianList = librarianService.findByStatus(status);
            List<LibrarianResponseDTO> responseDTOs = librarianList.stream()
                    .map(librarianMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Search Librarians by Name
    @Operation(
            summary = "Search librarians by name",
            description = "Searches for librarians whose names contain the given string (case-insensitive). Supports pagination and sorting."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search/name")
    public ResponseEntity<?> searchLibrariansByName(
            @Parameter(description = "Name search query", required = true, example = "Jane")
            @RequestParam String name,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Librarian> librarianPage = librarianService.findByNameContainingIgnoreCase(name, pageable);
            Page<LibrarianResponseDTO> responsePage = librarianPage.map(librarianMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Librarian> librarianList = librarianService.findByNameContainingIgnoreCase(name);
            List<LibrarianResponseDTO> responseDTOs = librarianList.stream()
                    .map(librarianMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Get Librarians by Status and Role
    @Operation(
            summary = "Filter librarians by status and role",
            description = "Retrieves librarians filtered by both status and role. Useful for finding active admins or inactive staff. Supports pagination and sorting."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarians retrieved successfully")
    })
    @GetMapping("/search/status-role")
    public ResponseEntity<?> getLibrariansByStatusAndRole(
            @Parameter(description = "Librarian status", required = true, example = "ACTIVE")
            @RequestParam Status status,
            @Parameter(description = "Librarian role", required = true, example = "ADMIN")
            @RequestParam Role role,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        if (page != null && size != null) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Librarian> librarianPage = librarianService.findByStatusAndRole(status, role, pageable);
            Page<LibrarianResponseDTO> responsePage = librarianPage.map(librarianMapper::toResponseDTO);
            return new ResponseEntity<>(responsePage, HttpStatus.OK);
        } else {
            List<Librarian> librarianList = librarianService.findByStatusAndRole(status, role);
            List<LibrarianResponseDTO> responseDTOs = librarianList.stream()
                    .map(librarianMapper::toResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        }
    }
    
    // Activate Librarian
    @Operation(
            summary = "Activate librarian account",
            description = "Changes a librarian's status to ACTIVE. Use this to restore inactive accounts."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarian activated successfully",
                    content = @Content(schema = @Schema(implementation = LibrarianResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Librarian not found")
    })
    @PostMapping("/{id}/activate")
    public ResponseEntity<LibrarianResponseDTO> activateLibrarian(
            @Parameter(description = "Librarian ID", required = true, example = "1")
            @PathVariable Long id) {
        Librarian activatedLibrarian = librarianService.activateLibrarian(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(activatedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Deactivate Librarian
    @Operation(
            summary = "Deactivate librarian account",
            description = "Changes a librarian's status to INACTIVE. Use this to temporarily disable accounts."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarian deactivated successfully",
                    content = @Content(schema = @Schema(implementation = LibrarianResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Librarian not found")
    })
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<LibrarianResponseDTO> deactivateLibrarian(
            @Parameter(description = "Librarian ID", required = true, example = "1")
            @PathVariable Long id) {
        Librarian deactivatedLibrarian = librarianService.deactivateLibrarian(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(deactivatedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Suspend Librarian
    @Operation(
            summary = "Suspend librarian account",
            description = "Changes a librarian's status to INACTIVE (suspension). Use this for disciplinary actions."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarian suspended successfully",
                    content = @Content(schema = @Schema(implementation = LibrarianResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Librarian not found")
    })
    @PostMapping("/{id}/suspend")
    public ResponseEntity<LibrarianResponseDTO> suspendLibrarian(
            @Parameter(description = "Librarian ID", required = true, example = "1")
            @PathVariable Long id) {
        Librarian suspendedLibrarian = librarianService.suspendLibrarian(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(suspendedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Change Password
    @Operation(
            summary = "Change librarian password",
            description = "Updates a librarian's password after verifying the old password. New password must be at least 6 characters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password - Old password incorrect or new password too short"),
            @ApiResponse(responseCode = "404", description = "Librarian not found")
    })
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Parameter(description = "Librarian ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Current password", required = true, example = "OldPass123")
            @RequestParam String oldPassword,
            @Parameter(description = "New password (min 6 characters)", required = true, example = "NewSecurePass456")
            @RequestParam String newPassword) {
        librarianService.changePassword(id, oldPassword, newPassword);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Promote to Admin
    @Operation(
            summary = "Promote librarian to admin",
            description = "Changes a librarian's role from STAFF to ADMIN. Grants administrative privileges."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarian promoted to admin successfully",
                    content = @Content(schema = @Schema(implementation = LibrarianResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Librarian is already an admin"),
            @ApiResponse(responseCode = "404", description = "Librarian not found")
    })
    @PostMapping("/{id}/promote")
    public ResponseEntity<LibrarianResponseDTO> promoteToAdmin(
            @Parameter(description = "Librarian ID", required = true, example = "1")
            @PathVariable Long id) {
        Librarian promotedLibrarian = librarianService.promoteToAdmin(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(promotedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Demote to Staff
    @Operation(
            summary = "Demote librarian to staff",
            description = "Changes a librarian's role from ADMIN to STAFF. Removes administrative privileges."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Librarian demoted to staff successfully",
                    content = @Content(schema = @Schema(implementation = LibrarianResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Librarian is already a staff member"),
            @ApiResponse(responseCode = "404", description = "Librarian not found")
    })
    @PostMapping("/{id}/demote")
    public ResponseEntity<LibrarianResponseDTO> demoteToStaff(
            @Parameter(description = "Librarian ID", required = true, example = "1")
            @PathVariable Long id) {
        Librarian demotedLibrarian = librarianService.demoteToStaff(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(demotedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Check if Email Exists
    @Operation(
            summary = "Check if email exists",
            description = "Validates whether an email address is already registered in the system. Useful for registration validation."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validation completed")
    })
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(
            @Parameter(description = "Email address to check", required = true, example = "john.doe@library.com")
            @PathVariable String email) {
        boolean exists = librarianService.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if Phone Number Exists
    @Operation(
            summary = "Check if phone number exists",
            description = "Validates whether a phone number is already registered in the system. Useful for registration validation."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validation completed")
    })
    @GetMapping("/exists/phone/{phoneNumber}")
    public ResponseEntity<Map<String, Boolean>> checkPhoneNumberExists(
            @Parameter(description = "Phone number to check", required = true, example = "555-0123")
            @PathVariable String phoneNumber) {
        boolean exists = librarianService.existsByPhoneNumber(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Count Librarians by Role
    @Operation(
            summary = "Count librarians by role",
            description = "Returns the total number of librarians with a specific role. Useful for dashboard statistics and reporting."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    @GetMapping("/count/role/{role}")
    public ResponseEntity<Map<String, Long>> countLibrariansByRole(
            @Parameter(description = "Librarian role", required = true, example = "ADMIN")
            @PathVariable Role role) {
        long count = librarianService.countByRole(role);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Count Librarians by Status
    @Operation(
            summary = "Count librarians by status",
            description = "Returns the total number of librarians with a specific status. Useful for dashboard statistics and reporting."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Map<String, Long>> countLibrariansByStatus(
            @Parameter(description = "Librarian status", required = true, example = "ACTIVE")
            @PathVariable Status status) {
        long count = librarianService.countByStatus(status);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
