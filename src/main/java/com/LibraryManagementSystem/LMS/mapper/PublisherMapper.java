package com.LibraryManagementSystem.LMS.mapper;

import com.LibraryManagementSystem.LMS.dto.PublisherRequestDTO;
import com.LibraryManagementSystem.LMS.dto.PublisherResponseDTO;
import com.LibraryManagementSystem.LMS.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {
    
    public PublisherResponseDTO toResponseDTO(Publisher publisher) {
        if (publisher == null) {
            return null;
        }
        
        PublisherResponseDTO dto = new PublisherResponseDTO();
        dto.setId(publisher.getId());
        dto.setName(publisher.getName());
        dto.setAddress(publisher.getAddress());
        dto.setEmail(publisher.getEmail());
        dto.setCountry(publisher.getCountry());
        
        return dto;
    }
    
    public Publisher toEntity(PublisherRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        Publisher publisher = new Publisher();
        publisher.setName(requestDTO.getName());
        publisher.setAddress(requestDTO.getAddress());
        publisher.setEmail(requestDTO.getEmail());
        publisher.setCountry(requestDTO.getCountry());
        
        return publisher;
    }
    
    public void updateEntityFromRequest(Publisher publisher, PublisherRequestDTO requestDTO) {
        if (publisher == null || requestDTO == null) {
            return;
        }
        
        if (requestDTO.getName() != null) {
            publisher.setName(requestDTO.getName());
        }
        if (requestDTO.getAddress() != null) {
            publisher.setAddress(requestDTO.getAddress());
        }
        if (requestDTO.getEmail() != null) {
            publisher.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getCountry() != null) {
            publisher.setCountry(requestDTO.getCountry());
        }
    }
}

