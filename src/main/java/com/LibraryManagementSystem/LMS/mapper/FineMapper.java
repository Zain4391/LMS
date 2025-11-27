package com.LibraryManagementSystem.LMS.mapper;

import com.LibraryManagementSystem.LMS.dto.FineRequestDTO;
import com.LibraryManagementSystem.LMS.dto.FineResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.entity.Fine;
import com.LibraryManagementSystem.LMS.enums.FineStatus;
import com.LibraryManagementSystem.LMS.repository.BorrowedRepository;
import org.springframework.stereotype.Component;

@Component
public class FineMapper {
    
    private final BorrowedMapper borrowedMapper;
    private final BorrowedRepository borrowedRepository;
    
    public FineMapper(BorrowedMapper borrowedMapper, BorrowedRepository borrowedRepository) {
        this.borrowedMapper = borrowedMapper;
        this.borrowedRepository = borrowedRepository;
    }
    
    public FineResponseDTO toResponseDTO(Fine fine) {
        if (fine == null) {
            return null;
        }
        
        FineResponseDTO dto = new FineResponseDTO();
        dto.setId(fine.getId());
        dto.setAmount(fine.getAmount());
        dto.setAssessedDate(fine.getAssessedDate());
        dto.setStatus(fine.getStatus());
        dto.setReason(fine.getReason());
        
        // Map Borrowed entity to BorrowedResponseDTO
        if (fine.getBorrowed() != null) {
            dto.setBorrowed(borrowedMapper.toResponseDTO(fine.getBorrowed()));
        }
        
        return dto;
    }
    
    public Fine toEntity(FineRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        Fine fine = new Fine();
        fine.setAmount(requestDTO.getAmount());
        fine.setAssessedDate(requestDTO.getAssessedDate());
        
        // Set status, default to PENDING if not provided
        if (requestDTO.getStatus() != null) {
            fine.setStatus(requestDTO.getStatus());
        } else {
            fine.setStatus(FineStatus.PENDING);
        }
        
        fine.setReason(requestDTO.getReason());
        
        // Fetch and set Borrowed entity
        if (requestDTO.getBorrowedId() != null) {
            Borrowed borrowed = borrowedRepository.findById(requestDTO.getBorrowedId())
                    .orElse(null);
            fine.setBorrowed(borrowed);
        }
        
        return fine;
    }
    
    public void updateEntityFromRequest(Fine fine, FineRequestDTO requestDTO) {
        if (fine == null || requestDTO == null) {
            return;
        }
        
        if (requestDTO.getAmount() != null) {
            fine.setAmount(requestDTO.getAmount());
        }
        
        if (requestDTO.getAssessedDate() != null) {
            fine.setAssessedDate(requestDTO.getAssessedDate());
        }
        
        if (requestDTO.getStatus() != null) {
            fine.setStatus(requestDTO.getStatus());
        }
        
        if (requestDTO.getReason() != null) {
            fine.setReason(requestDTO.getReason());
        }
        
        // Update Borrowed if borrowedId is provided
        if (requestDTO.getBorrowedId() != null) {
            Borrowed borrowed = borrowedRepository.findById(requestDTO.getBorrowedId())
                    .orElse(null);
            fine.setBorrowed(borrowed);
        }
    }
}

