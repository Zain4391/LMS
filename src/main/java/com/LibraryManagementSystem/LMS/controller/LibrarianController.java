package com.LibraryManagementSystem.LMS.controller;

import com.LibraryManagementSystem.LMS.dto.LibrarianRequestDTO;
import com.LibraryManagementSystem.LMS.dto.LibrarianResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Librarian;
import com.LibraryManagementSystem.LMS.enums.Role;
import com.LibraryManagementSystem.LMS.enums.Status;
import com.LibraryManagementSystem.LMS.mapper.LibrarianMapper;
import com.LibraryManagementSystem.LMS.service.interfaces.LibrarianService;
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
public class LibrarianController {
    
    private final LibrarianService librarianService;
    private final LibrarianMapper librarianMapper;
    
    public LibrarianController(LibrarianService librarianService, LibrarianMapper librarianMapper) {
        this.librarianService = librarianService;
        this.librarianMapper = librarianMapper;
    }
    
    // Create new Librarian
    @PostMapping
    public ResponseEntity<LibrarianResponseDTO> createLibrarian(@Valid @RequestBody LibrarianRequestDTO requestDTO) {
        Librarian librarian = librarianMapper.toEntity(requestDTO);
        Librarian createdLibrarian = librarianService.create(librarian);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(createdLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    // Get Librarian by ID
    @GetMapping("/{id}")
    public ResponseEntity<LibrarianResponseDTO> getLibrarianById(@PathVariable Long id) {
        Librarian librarian = librarianService.getById(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(librarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get all Librarians (with optional pagination)
    @GetMapping
    public ResponseEntity<?> getAllLibrarians(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
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
    @PutMapping("/{id}")
    public ResponseEntity<LibrarianResponseDTO> updateLibrarian(
            @PathVariable Long id,
            @Valid @RequestBody LibrarianRequestDTO requestDTO) {
        Librarian librarian = librarianMapper.toEntity(requestDTO);
        Librarian updatedLibrarian = librarianService.update(id, librarian);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(updatedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Delete Librarian by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrarian(@PathVariable Long id) {
        librarianService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Get Librarian by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<LibrarianResponseDTO> getLibrarianByEmail(@PathVariable String email) {
        Librarian librarian = librarianService.findByEmail(email);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(librarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Get Librarians by Role
    @GetMapping("/role/{role}")
    public ResponseEntity<?> getLibrariansByRole(
            @PathVariable Role role,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
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
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getLibrariansByStatus(
            @PathVariable Status status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
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
    @GetMapping("/search/name")
    public ResponseEntity<?> searchLibrariansByName(
            @RequestParam String name,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
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
    @GetMapping("/search/status-role")
    public ResponseEntity<?> getLibrariansByStatusAndRole(
            @RequestParam Status status,
            @RequestParam Role role,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "name") String sortBy,
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
    @PostMapping("/{id}/activate")
    public ResponseEntity<LibrarianResponseDTO> activateLibrarian(@PathVariable Long id) {
        Librarian activatedLibrarian = librarianService.activateLibrarian(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(activatedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Deactivate Librarian
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<LibrarianResponseDTO> deactivateLibrarian(@PathVariable Long id) {
        Librarian deactivatedLibrarian = librarianService.deactivateLibrarian(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(deactivatedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Suspend Librarian
    @PostMapping("/{id}/suspend")
    public ResponseEntity<LibrarianResponseDTO> suspendLibrarian(@PathVariable Long id) {
        Librarian suspendedLibrarian = librarianService.suspendLibrarian(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(suspendedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Change Password
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        librarianService.changePassword(id, oldPassword, newPassword);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Promote to Admin
    @PostMapping("/{id}/promote")
    public ResponseEntity<LibrarianResponseDTO> promoteToAdmin(@PathVariable Long id) {
        Librarian promotedLibrarian = librarianService.promoteToAdmin(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(promotedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Demote to Staff
    @PostMapping("/{id}/demote")
    public ResponseEntity<LibrarianResponseDTO> demoteToStaff(@PathVariable Long id) {
        Librarian demotedLibrarian = librarianService.demoteToStaff(id);
        LibrarianResponseDTO responseDTO = librarianMapper.toResponseDTO(demotedLibrarian);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    // Check if Email Exists
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@PathVariable String email) {
        boolean exists = librarianService.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Check if Phone Number Exists
    @GetMapping("/exists/phone/{phoneNumber}")
    public ResponseEntity<Map<String, Boolean>> checkPhoneNumberExists(@PathVariable String phoneNumber) {
        boolean exists = librarianService.existsByPhoneNumber(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Count Librarians by Role
    @GetMapping("/count/role/{role}")
    public ResponseEntity<Map<String, Long>> countLibrariansByRole(@PathVariable Role role) {
        long count = librarianService.countByRole(role);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Count Librarians by Status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Map<String, Long>> countLibrariansByStatus(@PathVariable Status status) {
        long count = librarianService.countByStatus(status);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
