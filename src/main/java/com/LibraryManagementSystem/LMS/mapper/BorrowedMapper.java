package com.LibraryManagementSystem.LMS.mapper;

import com.LibraryManagementSystem.LMS.dto.BorrowedRequestDTO;
import com.LibraryManagementSystem.LMS.dto.BorrowedResponseDTO;
import com.LibraryManagementSystem.LMS.dto.UserSummaryDTO;
import com.LibraryManagementSystem.LMS.entity.BookCopy;
import com.LibraryManagementSystem.LMS.entity.Borrowed;
import com.LibraryManagementSystem.LMS.entity.User;
import com.LibraryManagementSystem.LMS.repository.BookCopyRepository;
import com.LibraryManagementSystem.LMS.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class BorrowedMapper {
    
    private final BookCopyMapper bookCopyMapper;
    private final UserRepository userRepository;
    private final BookCopyRepository bookCopyRepository;
    
    public BorrowedMapper(BookCopyMapper bookCopyMapper, 
                          UserRepository userRepository, 
                          BookCopyRepository bookCopyRepository) {
        this.bookCopyMapper = bookCopyMapper;
        this.userRepository = userRepository;
        this.bookCopyRepository = bookCopyRepository;
    }
    
    public BorrowedResponseDTO toResponseDTO(Borrowed borrowed) {
        if (borrowed == null) {
            return null;
        }
        
        BorrowedResponseDTO dto = new BorrowedResponseDTO();
        dto.setId(borrowed.getId());
        dto.setBorrowDate(borrowed.getBorrowDate());
        dto.setDueDate(borrowed.getDueDate());
        dto.setReturnDate(borrowed.getReturnDate());
        dto.setStatus(borrowed.getStatus());
        
        // Map User entity to UserSummaryDTO
        if (borrowed.getUser() != null) {
            dto.setUser(toUserSummaryDTO(borrowed.getUser()));
        }
        
        // Map BookCopy entity to BookCopyResponseDTO
        if (borrowed.getBookCopy() != null) {
            dto.setBookCopy(bookCopyMapper.toResponseDTO(borrowed.getBookCopy()));
        }
        
        return dto;
    }
    
    public Borrowed toEntity(BorrowedRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        Borrowed borrowed = new Borrowed();
        borrowed.setBorrowDate(requestDTO.getBorrowDate());
        borrowed.setDueDate(requestDTO.getDueDate());
        
        // Fetch and set User entity
        if (requestDTO.getUserId() != null) {
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElse(null);
            borrowed.setUser(user);
        }
        
        // Fetch and set BookCopy entity
        if (requestDTO.getBookCopyId() != null) {
            BookCopy bookCopy = bookCopyRepository.findById(requestDTO.getBookCopyId())
                    .orElse(null);
            borrowed.setBookCopy(bookCopy);
        }
        
        return borrowed;
    }
    
    public void updateEntityFromRequest(Borrowed borrowed, BorrowedRequestDTO requestDTO) {
        if (borrowed == null || requestDTO == null) {
            return;
        }
        
        if (requestDTO.getBorrowDate() != null) {
            borrowed.setBorrowDate(requestDTO.getBorrowDate());
        }
        if (requestDTO.getDueDate() != null) {
            borrowed.setDueDate(requestDTO.getDueDate());
        }
        
        // Update User if userId is provided
        if (requestDTO.getUserId() != null) {
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElse(null);
            borrowed.setUser(user);
        }
        
        // Update BookCopy if bookCopyId is provided
        if (requestDTO.getBookCopyId() != null) {
            BookCopy bookCopy = bookCopyRepository.findById(requestDTO.getBookCopyId())
                    .orElse(null);
            borrowed.setBookCopy(bookCopy);
        }
    }
    
    // Helper method to convert User entity to UserSummaryDTO
    private UserSummaryDTO toUserSummaryDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserSummaryDTO dto = new UserSummaryDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setMembershipDate(user.getMembershipDate());
        dto.setStatus(user.getStatus());
        
        return dto;
    }
}
