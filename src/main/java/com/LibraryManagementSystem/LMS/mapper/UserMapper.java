package com.LibraryManagementSystem.LMS.mapper;

import com.LibraryManagementSystem.LMS.dto.UserRequestDTO;
import com.LibraryManagementSystem.LMS.dto.UserResponseDTO;
import com.LibraryManagementSystem.LMS.entity.User;
import com.LibraryManagementSystem.LMS.enums.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserMapper {
    
    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setMembershipDate(user.getMembershipDate());
        dto.setStatus(user.getStatus());
        
        return dto;
    }
    
    public User toEntity(UserRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(requestDTO.getPassword());
        user.setPhoneNumber(requestDTO.getPhoneNumber());
        user.setAddress(requestDTO.getAddress());
        
        // Set membership date, default to current date if not provided
        if (requestDTO.getMembershipDate() != null) {
            user.setMembershipDate(requestDTO.getMembershipDate());
        } else {
            user.setMembershipDate(LocalDate.now());
        }
        
        // Set status, default to ACTIVE if not provided
        if (requestDTO.getStatus() != null) {
            user.setStatus(requestDTO.getStatus());
        } else {
            user.setStatus(Status.ACTIVE);
        }
        
        return user;
    }
    
    public void updateEntityFromRequest(User user, UserRequestDTO requestDTO) {
        if (user == null || requestDTO == null) {
            return;
        }
        
        if (requestDTO.getName() != null) {
            user.setName(requestDTO.getName());
        }
        if (requestDTO.getEmail() != null) {
            user.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getPassword() != null) {
            user.setPassword(requestDTO.getPassword());
        }
        if (requestDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(requestDTO.getPhoneNumber());
        }
        if (requestDTO.getAddress() != null) {
            user.setAddress(requestDTO.getAddress());
        }
        if (requestDTO.getMembershipDate() != null) {
            user.setMembershipDate(requestDTO.getMembershipDate());
        }
        if (requestDTO.getStatus() != null) {
            user.setStatus(requestDTO.getStatus());
        }
    }
}

