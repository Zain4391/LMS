package com.LibraryManagementSystem.LMS.mapper;

import com.LibraryManagementSystem.LMS.dto.LibrarianRequestDTO;
import com.LibraryManagementSystem.LMS.dto.LibrarianResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Librarian;
import com.LibraryManagementSystem.LMS.enums.Role;
import com.LibraryManagementSystem.LMS.enums.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LibrarianMapper {
    
    public LibrarianResponseDTO toResponseDTO(Librarian librarian) {
        if (librarian == null) {
            return null;
        }
        
        LibrarianResponseDTO dto = new LibrarianResponseDTO();
        dto.setId(librarian.getId());
        dto.setName(librarian.getName());
        dto.setEmail(librarian.getEmail());
        dto.setPhoneNumber(librarian.getPhoneNumber());
        dto.setAddress(librarian.getAddress());
        dto.setRole(librarian.getRole());
        dto.setHireDate(librarian.getHireDate());
        dto.setStatus(librarian.getStatus());
        
        return dto;
    }
    
    public Librarian toEntity(LibrarianRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        Librarian librarian = new Librarian();
        librarian.setName(requestDTO.getName());
        librarian.setEmail(requestDTO.getEmail());
        librarian.setPassword(requestDTO.getPassword());
        librarian.setPhoneNumber(requestDTO.getPhoneNumber());
        librarian.setAddress(requestDTO.getAddress());
        
        // Set role, default to STAFF if not provided
        if (requestDTO.getRole() != null) {
            librarian.setRole(requestDTO.getRole());
        } else {
            librarian.setRole(Role.STAFF);
        }
        
        // Set hire date, default to current date if not provided
        if (requestDTO.getHireDate() != null) {
            librarian.setHireDate(requestDTO.getHireDate());
        } else {
            librarian.setHireDate(LocalDate.now());
        }
        
        // Set status, default to ACTIVE if not provided
        if (requestDTO.getStatus() != null) {
            librarian.setStatus(requestDTO.getStatus());
        } else {
            librarian.setStatus(Status.ACTIVE);
        }
        
        return librarian;
    }
    
    public void updateEntityFromRequest(Librarian librarian, LibrarianRequestDTO requestDTO) {
        if (librarian == null || requestDTO == null) {
            return;
        }
        
        if (requestDTO.getName() != null) {
            librarian.setName(requestDTO.getName());
        }
        if (requestDTO.getEmail() != null) {
            librarian.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getPassword() != null) {
            librarian.setPassword(requestDTO.getPassword());
        }
        if (requestDTO.getPhoneNumber() != null) {
            librarian.setPhoneNumber(requestDTO.getPhoneNumber());
        }
        if (requestDTO.getAddress() != null) {
            librarian.setAddress(requestDTO.getAddress());
        }
        if (requestDTO.getRole() != null) {
            librarian.setRole(requestDTO.getRole());
        }
        if (requestDTO.getHireDate() != null) {
            librarian.setHireDate(requestDTO.getHireDate());
        }
        if (requestDTO.getStatus() != null) {
            librarian.setStatus(requestDTO.getStatus());
        }
    }
}
